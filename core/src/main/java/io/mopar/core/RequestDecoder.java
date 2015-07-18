package io.mopar.core;

/**
 * @author Hadyn Fitzgerald
 */
public interface RequestDecoder<V extends Request, T> {

    /**
     * Decodes a message.
     *
     * @param message The message to decode.
     * @return The decoded value.
     */
    V decode(T message);
}
