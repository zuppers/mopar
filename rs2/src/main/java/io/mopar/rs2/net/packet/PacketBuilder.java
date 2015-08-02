package io.mopar.rs2.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketBuilder {

    private static final int[] BITMASKS = {
            0x0, 0x1, 0x3, 0x7,
            0xf, 0x1f, 0x3f, 0x7f,
            0xff, 0x1ff, 0x3ff, 0x7ff,
            0xfff, 0x1fff, 0x3fff, 0x7fff,
            0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
            0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
            0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
            0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
            -1 };

    public int writerIndex() {
        return buf.writerIndex();
    }

    public PacketBuilder putByteA(int pos, int i) {
        buf.setByte(pos, i + 128);
        return this;
    }

    public PacketBuilder writeLong(long l) {
        buf.writeLong(l);
        return this;
    }

    public PacketBuilder writeBytes(ByteBuf buffer) {
        buf.writeBytes(buffer);
        return this;
    }


    enum AccessMode {BYTE_ACCESS, BIT_ACCESS}

    /**
     * The meta data.
     */
    private PacketMetaData meta;

    /**
     * The buffer.
     */
    private ByteBuf buf;

    /**
     * The access.
     */
    private AccessMode mode = AccessMode.BYTE_ACCESS;

    /**
     * The bit position.
     */
    private int bitPosition;

    /**
     * Constructs a new {@link PacketBuilder};
     *
     * @param meta The packet meta data.
     * @param buf The packet buffer.
     */
    private PacketBuilder(PacketMetaData meta, ByteBuf buf) {
        this.meta = meta;
        this.buf = buf;
    }

    /**
     *
     */
    public void switchToByteAccess() {
        if(mode == AccessMode.BYTE_ACCESS) {
            throw new IllegalStateException("Already in byte access mode");
        }
        mode = AccessMode.BYTE_ACCESS;
        buf.writerIndex((bitPosition + 7) / 8);
    }

    /**
     *
     */
    public void switchToBitAccess() {
        if(mode == AccessMode.BIT_ACCESS) {
            throw new IllegalStateException("Already in bit access mode");
        }
        bitPosition = buf.writerIndex() * 8;
        mode = AccessMode.BIT_ACCESS;
    }

    /**
     * Writes a byte.
     *
     * @param i The integer value.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeByte(int i) {
        buf.writeByte(i);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeByteA(int i) {
        buf.writeByte(i + 128);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeByteS(int i) {
        buf.writeByte(128 - i);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeByteN(int i) {
        buf.writeByte(-i);
        return this;
    }

    /**
     * Writes a short.
     *
     * @param i The integer value.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeShort(int i) {
        buf.writeShort(i);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeShortA(int i) {
        buf.writeByte(i >> 8);
        buf.writeByte(i + 128);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeLEShort(int i) {
        buf.writeByte(i);
        buf.writeByte(i >> 8);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeLEShortA(int i) {
        buf.writeByte(i + 128);
        buf.writeByte(i >> 8);
        return this;
    }

    /**
     * Writes an integer.
     *
     * @param value The integer value.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeInt(int value) {
        buf.writeInt(value);
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeLEInt(int i) {
        buf.writeByte((byte)  i);
        buf.writeByte((byte) (i >> 8));
        buf.writeByte((byte) (i >> 16));
        buf.writeByte((byte) (i >> 24));
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeMEInt(int i) {
        buf.writeByte((byte) (i >> 8));
        buf.writeByte((byte)  i);
        buf.writeByte((byte) (i >> 24));
        buf.writeByte((byte) (i >> 16));
        return this;
    }

    /**
     *
     * @param i
     * @return
     */
    public PacketBuilder writeIMEInt(int i) {
        buf.writeByte((byte) (i >> 16));
        buf.writeByte((byte) (i >> 24));
        buf.writeByte((byte)  i);
        buf.writeByte((byte) (i >> 8));
        return this;
    }


    /**
     * Writes a boolean.
     *
     * @param bool The boolean value.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeBoolean(boolean bool) {
        buf.writeBoolean(bool);
        return this;
    }

    /**
     * Writes a smart.
     *
     * @param i The integer value.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeSmart(int i) {
        if(i >= -128 && i < 128) {
            buf.writeByte(i);
        } else if(i >= 128 && i <= 32767) {
           buf.writeShort(0x8000 | i);
        } else {
            throw new IllegalStateException("Bad value");
        }
        return this;
    }

    /**
     *
     * @param bytes
     */
    public void writeBytesReverse(byte[] bytes) {
        for (int i = bytes.length - 1; i >= 0; i--) {
            buf.writeByte(bytes[i]);
        }
    }

    /**
     * Writes a string.
     *
     * @param str The string to write.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeJstr(String str) {
        buf.writeBytes(str.getBytes(StandardCharsets.ISO_8859_1));
        buf.writeByte('\0');
        return this;
    }

    /**
     * Writes a string.
     *
     * @param str The string to write.
     * @return This instance of the packet builder for chaining.
     */
    public PacketBuilder writeJstr2(String str) {
        buf.writeByte(0);                           // Version
        buf.writeBytes(str.getBytes(StandardCharsets.ISO_8859_1));
        buf.writeByte('\0');
        return this;
    }

    /**
     *
     * @param b
     */
    public void writeBit(boolean b) {
        writeBits(1, b ? 1 : 0);
    }

    /**
     *
     * @param numBits
     * @param value
     */
    public void writeBits(int numBits, int value) {
        int bytePos = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        bitPosition += numBits;

        int requiredSpace = bytePos - buf.writerIndex() + 1;
        requiredSpace += (numBits + 7) / 8;
        buf.ensureWritable(requiredSpace);

        for (; numBits > bitOffset; bitOffset = 8) {
            int tmp = buf.getByte(bytePos);
            tmp &= ~BITMASKS[bitOffset];
            tmp |= (value >> (numBits-bitOffset)) & BITMASKS[bitOffset];
            buf.setByte(bytePos++, tmp);
            numBits -= bitOffset;
        }
        if (numBits == bitOffset) {
            int tmp = buf.getByte(bytePos);
            tmp &= ~BITMASKS[bitOffset];
            tmp |= value & BITMASKS[bitOffset];
            buf.setByte(bytePos, tmp);
        } else {
            int tmp = buf.getByte(bytePos);
            tmp &= ~(BITMASKS[numBits] << (bitOffset - numBits));
            tmp |= (value & BITMASKS[numBits]) << (bitOffset - numBits);
            buf.setByte(bytePos, tmp);
        }
    }

    /**
     *
     * @return
     */
    public Packet build() {
        return new Packet(meta, buf);
    }

    /**
     *
     * @param id
     * @param name
     * @param length
     * @param alloc
     * @return
     */
    public static PacketBuilder create(int id, String name, int length, ByteBufAllocator alloc) {
        return create(new PacketMetaData(id, name, length), alloc);
    }

    /**
     *
     * @param meta
     * @param alloc
     * @return
     */
    public static PacketBuilder create(PacketMetaData meta, ByteBufAllocator alloc) {
        ByteBuf buf;
        switch (meta.getLength()) {
            case PacketMetaData.VAR_BYTE_LENGTH:
            case PacketMetaData.VAR_SHORT_LENGTH:
                buf = alloc.buffer(256);
                break;

            case 0:
                buf = Unpooled.EMPTY_BUFFER;
                break;

            default:
                buf = alloc.buffer(meta.getLength(), meta.getLength());
                break;
        }
        return new PacketBuilder(meta, buf);
    }
}
