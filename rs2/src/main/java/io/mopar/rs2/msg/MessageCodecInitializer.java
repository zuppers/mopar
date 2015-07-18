package io.mopar.rs2.msg;

import io.mopar.rs2.net.packet.PacketMetaList;

/**
 * Created by hadyn on 6/25/2015.
 */
public interface MessageCodecInitializer {

    /**
     * Initializes the provided message codec pack.
     *
     * @param codec The codec.
     * @param incomingPackets The incoming packets.
     * @param outgoingPackets The outgoing packets.
     */
    void initialize(MessageCodec codec, PacketMetaList incomingPackets, PacketMetaList outgoingPackets);
}
