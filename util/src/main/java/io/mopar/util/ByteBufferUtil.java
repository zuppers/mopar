package io.mopar.util;

import java.nio.ByteBuffer;

/**
 * Created by hadyn on 6/29/2015.
 */
public class ByteBufferUtil {

    /**
     *
     * @param buffer
     * @return
     */
    public static int getUnsignedMedium(ByteBuffer buffer) {
        return (buffer.get() & 0xff) << 16 | (buffer.get() & 0xff) << 8 | buffer.get() & 0xff;
    }

    /**
     *
     * @param buffer
     * @param value
     */
    public static void putMedium(ByteBuffer buffer, int value) {
        buffer.put((byte) (value >> 16));
        buffer.put((byte) (value >>  8));
        buffer.put((byte)  value);
    }

    /**
     *
     * @param buffer
     * @param amount
     */
    public static void skip(ByteBuffer buffer, int amount) {
        buffer.position(buffer.position() + amount);
    }

    /**
     * Prevent instantiation;
     */
    private ByteBufferUtil() {}
}
