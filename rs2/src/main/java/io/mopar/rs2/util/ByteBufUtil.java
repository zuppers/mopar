package io.mopar.rs2.util;

import io.mopar.util.ByteBufferUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Hadyn Fitzgerald
 */
public class ByteBufUtil {

    /**
     *
     * @param buf
     * @return
     */
    public static int readUByteA(ByteBuf buf) {
        return (buf.readByte() - 128) & 0xff;
    }

    /**
     *
     * @param buf
     * @return
     */
    public static byte readByteA(ByteBuf buf) {
        return (byte) (buf.readByte() - 128);
    }

    /**
     *
     * @param buf
     * @return
     */
    public static int readByteS(ByteBuf buf) {
        return (byte) (128 - buf.readByte());
    }

    /**
     *
     * @param buf
     * @return
     */
    public static int readShortA(ByteBuf buf) {
        return (buf.readUnsignedByte() << 8) + ((buf.readByte() - 128) & 0xff);
    }

    /**
     * Reads a string.
     *
     * @param buf The buffer to read the string from.
     * @return The read string.
     */
    public static String readString(ByteBuf buf) {
        int start = buf.readerIndex();
        while(buf.readByte() != '\0') {}
        return buf.toString(start, buf.readerIndex() - start - 1, StandardCharsets.ISO_8859_1);
    }

    /**
     * Prevent instantiation;
     */
    private ByteBufUtil() {}
}
