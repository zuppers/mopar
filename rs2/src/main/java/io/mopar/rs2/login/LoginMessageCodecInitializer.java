package io.mopar.rs2.login;

import io.mopar.rs2.msg.MessageCodecInitializer;
import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.msg.login.LoginStatusCheck;
import io.mopar.rs2.msg.login.LoginRequestMessage;
import io.mopar.rs2.msg.login.ProfileMessage;
import io.mopar.rs2.net.packet.MalformedPacketException;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.mopar.rs2.util.ByteBufUtil.readString;

/**
 * @author Hadyn Fitzgerald
 */
public class LoginMessageCodecInitializer implements MessageCodecInitializer {

    /**
     * Constructs a new {@link LoginMessageCodecInitializer};
     */
    public LoginMessageCodecInitializer() {}

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
    private void registerMessageDecoders(MessageCodec codec, PacketMetaList incomingPackets) {
        codec.registerMessageDecoder(incomingPackets.getId("login_status"), this::decodeStatusCheck);
        codec.registerMessageDecoder(incomingPackets.getId("login_request_new"), this::decodeLoginRequest);
        codec.registerMessageDecoder(incomingPackets.getId("login_request_reconnect"), this::decodeLoginRequest);
    }

    /**
     * Decodes a status check message.
     *
     * @param packet The packet to decode the message from.
     * @return The decoded message.
     */
    private LoginStatusCheck decodeStatusCheck(Packet packet) {
        return new LoginStatusCheck();
    }

    /**
     * Decodes a login request message.
     *
     * @param packet The packet to decode the request from.
     * @return The decoded message.
     */
    private LoginRequestMessage decodeLoginRequest(Packet packet) throws MalformedPacketException {
        LoginRequestMessage message = new LoginRequestMessage();

        ByteBuf buf = packet.getBuffer();

        buf.readInt();                                                              // Build
        buf.readByte();                                                             // ?
        buf.readBoolean();                                                          // Suppress advert
        buf.readBoolean();                                                          // ?

        // Parse the client display information
        message.setDisplayMode(buf.readUnsignedByte());
        buf.readShort();                                                            // View width
        buf.readShort();                                                            // View height
        buf.readByte();                                                             // Samples

        buf.skipBytes(24);                                                          // UID

        readString(buf);                                                            // Settings

        buf.readInt();                                                              // Affiliate id

        buf.readInt();                                                              // Display settings

        buf.readUnsignedShort();                                                    // Packet counter


        for(int i = 0; i < 28; i++) {                                                // Volume checksums
            buf.readInt();
        }

        ByteBuf secureBuf = buf.readBytes(buf.readUnsignedByte());

        if(secureBuf.readByte() != 0xA) {
            throw new MalformedPacketException("Invalid check byte");
        }

        // Parse the cipher keys
        int[] cipherKeys = new int[4];
        for(int i = 0; i < 4; i++) {
            cipherKeys[i] = secureBuf.readInt();
        }

        message.setCipherKeys(cipherKeys);
        message.setUsername(secureBuf.readLong());
        message.setPassword(readString(secureBuf));

        return message;
    }

    /**
     * Registers all of the message encoders to the provided codec.
     *
     * @param codec The message codec.
     * @param outgoingPackets The outgoing packets.
     */
    private void registerMessageEncoders(MessageCodec codec, PacketMetaList outgoingPackets) {
        codec.registerMessageEncoder(ProfileMessage.class, this::encodeProfileMessage);
    }

    /**
     *
     * @param alloc
     * @param outgoingPackets
     * @param message
     */
    private Packet encodeProfileMessage(ByteBufAllocator alloc, PacketMetaList outgoingPackets, ProfileMessage message) {
        PacketBuilder builder = PacketBuilder.create(2, "profile_info", 14, alloc);
        builder.writeByte(2);                                                                   // Rights
        builder.writeByte(0);                                                                   // Black marks
        builder.writeBoolean(false);                                                            // ?
        builder.writeBoolean(false);                                                            // ?
        builder.writeBoolean(false);                                                            // ?
        builder.writeBoolean(false);                                                            // ?
        builder.writeBoolean(false);                                                            // ?
        builder.writeShort(0);                                                                  // Id
        builder.writeBoolean(false);                                                            // ?
        builder.writeBoolean(true);                                                            // ?
        return builder.build();
    }
}
