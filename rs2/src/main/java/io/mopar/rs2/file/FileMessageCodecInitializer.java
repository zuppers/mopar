package io.mopar.rs2.file;

import io.mopar.rs2.msg.MessageCodecInitializer;
import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.msg.handshake.FileServiceHandshakeMessage;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketMetaList;
/**
 * @author Hadyn Fitzgerald
 */
public class FileMessageCodecInitializer implements MessageCodecInitializer {

    /**
     * Constructs a new {@link FileMessageCodecInitializer};
     */
    public FileMessageCodecInitializer() {}

    /**
     * Initializes the codec.
     *
     * @param codec The codec.
     * @param incomingPackets The incoming packets.
     * @param outgoingPackets The outgoing packets.
     */
    @Override
    public void initialize(MessageCodec codec, PacketMetaList incomingPackets, PacketMetaList outgoingPackets) {
        registerMessageDecoders(codec, incomingPackets);
        registerMessageEncoders(codec, outgoingPackets);
    }

    /**
     * Register all of the message decoders to the provided codec.
     *
     * @param codec           The message codec.
     * @param incomingPackets The incoming packets.
     */
    private void registerMessageDecoders(MessageCodec codec, PacketMetaList incomingPackets) {
        codec.registerMessageDecoder(incomingPackets.getId("file_handshake"), this::decodeFileHandshake);
    }

    /**
     * Decodes an update service handshake message.
     *
     * @param packet The packet to decode the message from.
     * @return The decoded update service handshake message.
     */
    private FileServiceHandshakeMessage decodeFileHandshake(Packet packet) {
        return new FileServiceHandshakeMessage(packet.getBuffer().readInt());
    }

    /**
     * Registers all of the message encoders to the provided codec.
     *
     * @param codec           The message codec.
     * @param outgoingPackets The outgoing packets.
     */
    private void registerMessageEncoders(MessageCodec codec, PacketMetaList outgoingPackets) {}
}
