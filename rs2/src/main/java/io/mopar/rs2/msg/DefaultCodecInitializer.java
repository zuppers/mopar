package io.mopar.rs2.msg;

import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author Hadyn Fitzgerald
 */
public class DefaultCodecInitializer implements MessageCodecInitializer {

    /**
     * Constructs a new {@link DefaultCodecInitializer};
     */
    public DefaultCodecInitializer() {}

    @Override
    public void initialize(MessageCodec codec, PacketMetaList incomingPackets, PacketMetaList outgoingPackets) {
        registerMessageDecoders(codec, incomingPackets);
        registerMessageEncoders(codec, outgoingPackets);
    }

    /**
     * Register all of the message decoders to the provided codec.
     *
     * @param codec The message codec.
     * @param incomingPackets The incoming packets.
     */
    private void registerMessageDecoders(MessageCodec codec, PacketMetaList incomingPackets) {}

    /**
     * Registers all of the message encoders to the provided codec.
     *
     * @param codec The message codec.
     * @param outgoingPackets The outgoing packets.
     */
    private void registerMessageEncoders(MessageCodec codec, PacketMetaList outgoingPackets) {
        codec.registerMessageEncoder(StatusMessage.class, this::encodeStatusResponse);
    }

    /**
     * Encodes a status response message.
     *
     * @param alloc The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param msg The message to encode.
     * @return The encoded packet.
     */
    private Packet encodeStatusResponse(ByteBufAllocator alloc, PacketMetaList outgoingPackets, StatusMessage msg) {
        return PacketBuilder.create(msg.getId(), msg.getName(), 0, alloc).build();
    }
}
