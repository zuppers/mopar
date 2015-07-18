package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;
import io.mopar.rs2.net.packet.MalformedPacketException;
import io.mopar.rs2.net.packet.Packet;

/**
 * @author Hadyn Fitzgerald
 */
public interface MessageDecoder<T extends Message> {

    /**
     * Decodes a message from the provided packet.
     *
     * @param packet The packet to decode the message from.
     * @return The decoded message.
     */
    T decode(Packet packet) throws MalformedPacketException;
}
