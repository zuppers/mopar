package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class ChatMessage extends Message {

    public static final int HUFFMAN_ENCODING = 0;

    /**
     * The submitPublicChat encoding.
     */
    private int encoding;

    /**
     * The color.
     */
    private int color;

    /**
     * The effect.
     */
    private int effect;

    /**
     * The bytes.
     */
    private byte[] bytes;

    /**
     * Constructs a new {@link ChatMessage};
     *
     * @param encoding The encoding.
     * @param color The color.
     * @param effect The effect.
     * @param bytes The bytes.
     */
    public ChatMessage(int encoding, int color, int effect, byte[] bytes) {
        this.encoding = encoding;
        this.color = color;
        this.effect = effect;
        this.bytes = bytes;
    }

    /**
     * Gets the encoding.
     *
     * @return the encoding.
     */
    public int getEncoding() {
        return encoding;
    }

    /**
     * Gets the color.
     *
     * @return the color.
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets the effect.
     *
     * @return the effect.
     */
    public int getEffect() {
        return effect;
    }

    /**
     * Gets the bytes.
     *
     * @return the bytes.
     */
    public byte[] getBytes() {
        return bytes;
    }
}
