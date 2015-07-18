package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author Hadyn Fitzgerald
 */
public interface MessageEncoder<T extends Message> {

    /**
     * Encodes a message to a packet.
     *
     * @param allocator The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param message The message to encode.
     * @return The encoded packet.
     */
    Packet encode(ByteBufAllocator allocator, PacketMetaList outgoingPackets, T message);
}
