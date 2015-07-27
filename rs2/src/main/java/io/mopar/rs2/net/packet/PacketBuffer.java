package io.mopar.rs2.net.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketBuffer {

    private static final int[] BITMASKS = {
            0x0, 0x1, 0x3, 0x7,
            0xf, 0x1f, 0x3f, 0x7f,
            0xff, 0x1ff, 0x3ff, 0x7ff,
            0xfff, 0x1fff, 0x3fff, 0x7fff,
            0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
            0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
            0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
            0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };

    /**
     *
     */
    enum AccessMode { BYTE_ACCESS, BIT_ACCESS }

    /**
     * The buffer.
     */
    private ByteBuf buf;

    /**
     * The access mode.
     */
    private AccessMode mode = AccessMode.BYTE_ACCESS;

    /**
     * The bit position.
     */
    private int bitPosition;

    /**
     * Constructs a new {@link PacketBuffer};
     *
     * @param buf the buffer.
     */
    protected PacketBuffer(ByteBuf buf) {
        this.buf = buf;
    }

    /**
     *
     */
    public void switchToByteAccess() {
        if (mode == AccessMode.BYTE_ACCESS) {
            throw new IllegalStateException("Already in byte access mode");
        }
        mode = AccessMode.BYTE_ACCESS;
        buf.writerIndex((bitPosition + 7) / 8);
    }

    /**
     *
     */
    public void switchToBitAccess() {
        if (mode == AccessMode.BIT_ACCESS) {
            throw new IllegalStateException("Already in bit access mode");
        }
        bitPosition = buf.writerIndex() * 8;
        mode = AccessMode.BIT_ACCESS;
    }

    /**
     *
     * @return
     */
    public int writerIndex() {
        return buf.writerIndex();
    }

    /**
     * @param buf
     * @return
     */
    public static int readUByteA(ByteBuf buf) {
        return (buf.readByte() - 128) & 0xff;
    }

    /**
     * @param buf
     * @return
     */
    public static byte readByteA(ByteBuf buf) {
        return (byte) (buf.readByte() - 128);
    }

    /**
     * @param buf
     * @return
     */
    public static int readByteS(ByteBuf buf) {
        return (byte) (128 - buf.readByte());
    }

    /**
     * @param buf
     * @return
     */
    public static int readShortA(ByteBuf buf) {
        return (buf.readUnsignedByte() << 8) + ((buf.readByte() - 128) & 0xff);
    }

    /**
     * @param buf
     * @return
     */
    public static int readLEShort(ByteBuf buf) {
        return buf.readUnsignedByte() + (buf.readUnsignedByte() << 8);
    }

    /**
     * @param buf
     * @return
     */
    public static int readLEInt(ByteBuf buf) {
        return buf.readUnsignedByte() + (buf.readUnsignedByte() << 8) + (buf.readUnsignedByte() << 16) + (buf.readUnsignedByte() << 24);
    }

    /**
     * @param buf
     * @return
     */
    public static int readMEInt(ByteBuf buf) {
        return +(buf.readUnsignedByte() << 8) + buf.readUnsignedByte() + (buf.readUnsignedByte() << 24) + (buf.readUnsignedByte() << 16);
    }

    /**
     * Reads a string.
     *
     * @param buf The buffer to read the string from.
     * @return The read string.
     */
    public static String readString(ByteBuf buf) {
        int start = buf.readerIndex();
        while (buf.readByte() != '\0') {
        }
        return buf.toString(start, buf.readerIndex() - start - 1, StandardCharsets.ISO_8859_1);
    }

    /**
     * Writes a byte.
     *
     * @param i The integer value.
     */
    public void writeByte(int i) {
        buf.writeByte(i);

    }

    /**
     * @param i
     */
    public void writeByteA(int i) {
        buf.writeByte(i + 128);

    }

    /**
     * @param i
     */
    public void writeByteS(int i) {
        buf.writeByte(128 - i);
    }

    /**
     * @param i
     */
    public void writeByteN(int i) {
        buf.writeByte(-i);
    }

    /**
     * Writes a short.
     *
     * @param s The short value.
     */
    public void writeShort(int s) {
        buf.writeShort(s);
    }

    /**
     * @param i
     */
    public void writeShortA(int i) {
        buf.writeByte(i >> 8);
        buf.writeByte(i + 128);

    }

    /**
     * @param i
     */
    public void writeLEShort(int i) {
        buf.writeByte(i);
        buf.writeByte(i >> 8);
    }

    /**
     * @param i
     * @return
     */
    public void writeLEShortA(int i) {
        buf.writeByte(i + 128);
        buf.writeByte(i >> 8);

    }

    /**
     * Writes an integer.
     *
     * @param value The integer value.
     * @return This instance of the packet builder for chaining.
     */
    public void writeInt(int value) {
        buf.writeInt(value);

    }

    /**
     * @param i
     * @return
     */
    public void writeLEInt(int i) {
        buf.writeByte((byte) i);
        buf.writeByte((byte) (i >> 8));
        buf.writeByte((byte) (i >> 16));
        buf.writeByte((byte) (i >> 24));

    }

    /**
     * @param i
     * @return
     */
    public void writeMEInt(int i) {
        buf.writeByte((byte) (i >> 8));
        buf.writeByte((byte) i);
        buf.writeByte((byte) (i >> 24));
        buf.writeByte((byte) (i >> 16));

    }

    /**
     * @param i
     * @return
     */
    public void writeIMEInt(int i) {
        buf.writeByte((byte) (i >> 16));
        buf.writeByte((byte) (i >> 24));
        buf.writeByte((byte) i);
        buf.writeByte((byte) (i >> 8));

    }


    /**
     * Writes a boolean.
     *
     * @param bool The boolean value.
     * @return This instance of the packet builder for chaining.
     */
    public void writeBoolean(boolean bool) {
        buf.writeBoolean(bool);

    }

    /**
     * Writes a smart.
     *
     * @param i The integer value.
     * @return This instance of the packet builder for chaining.
     */
    public void writeSmart(int i) {
        if (i < 128) {
            buf.writeByte(i);
        } else if (i >= 128 && i <= 32767) {
            buf.writeShort(0x8000 | i);
        } else {
            throw new IllegalStateException("Bad value");
        }

    }

    /**
     *
     * @param l
     */
    public void writeLong(long l) {
        buf.writeLong(l);
    }

    /**
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
    public void writeJstr(String str) {
        buf.writeBytes(str.getBytes(StandardCharsets.ISO_8859_1));
        buf.writeByte('\0');

    }

    /**
     * Writes a string.
     *
     * @param str The string to write.
     * @return This instance of the packet builder for chaining.
     */
    public void writeJstr2(String str) {
        buf.writeByte(0);                           // Version
        buf.writeBytes(str.getBytes(StandardCharsets.ISO_8859_1));
        buf.writeByte('\0');

    }

    /**
     * @param b
     */
    public void writeBit(boolean b) {
        writeBits(1, b ? 1 : 0);
    }

    /**
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
            tmp |= (value >> (numBits - bitOffset)) & BITMASKS[bitOffset];
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
     * Creates a new {@link PacketBuffer} from a byte buffer.
     *
     * @param buf the buffer.
     * @return the created buffer.
     */
    public static PacketBuffer wrap(ByteBuf buf) {
        return new PacketBuffer(buf);
    }
}
