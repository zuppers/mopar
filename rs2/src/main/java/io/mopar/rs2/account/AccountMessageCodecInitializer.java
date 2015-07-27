package io.mopar.rs2.account;

import io.mopar.core.Base37;
import io.mopar.core.msg.Message;
import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.msg.MessageCodecInitializer;
import io.mopar.rs2.msg.login.LoginRequestMessage;
import io.mopar.rs2.msg.login.LoginStatusCheck;
import io.mopar.rs2.msg.login.ProfileMessage;
import io.mopar.rs2.net.packet.MalformedPacketException;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.mopar.rs2.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.util.List;

import static io.mopar.rs2.util.ByteBufUtil.readString;

/**
 * @author Hadyn Fitzgerald
 */
public class AccountMessageCodecInitializer implements MessageCodecInitializer {

    @Override
    public void initialize(MessageCodec codec, PacketMetaList incomingPackets, PacketMetaList outgoingPackets) {
        registerMessageDecoders(codec, incomingPackets);
        registerMessageEncoders(codec, outgoingPackets);
    }

    /**
     *
     * @param codec
     * @param incomingPackets
     */
    private void registerMessageDecoders(MessageCodec codec, PacketMetaList incomingPackets) {
        codec.registerMessageDecoder(incomingPackets.getId("validate_personal_details"), this::decodeValidatePersonalDetails);
        codec.registerMessageDecoder(incomingPackets.getId("query_username"), this::decodeQueryUsername);
        codec.registerMessageDecoder(incomingPackets.getId("registration_request"), this::decodeRegistrationRequest);
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

    /**
     *
     * @param packet
     * @return
     */
    private ValidatePersonalDetailsMessage decodeValidatePersonalDetails(Packet packet) {
        return new ValidatePersonalDetailsMessage();
    }

    /**
     *
     * @param packet
     * @return
     */
    private QueryUsernameMessage decodeQueryUsername(Packet packet) {
        return new QueryUsernameMessage(packet.getBuffer().readLong());
    }

    /**
     *
     * @param packet
     * @return
     * @throws MalformedPacketException
     */
    private RegistrationRequestMessage decodeRegistrationRequest(Packet packet) throws MalformedPacketException {
        RegistrationRequestMessage message = new RegistrationRequestMessage();

        ByteBuf buf = packet.getBuffer();
        int size = buf.readUnsignedByte();

        ByteBuf secureBuffer = buf.readBytes(size);
        if(secureBuffer.readUnsignedByte() != 10) {
            throw new MalformedPacketException("Invalid check");
        }
        secureBuffer.readUnsignedShort();
        secureBuffer.readUnsignedShort();

        message.setUsername(secureBuffer.readLong());

        secureBuffer.readInt();

        message.setPassword(ByteBufUtil.readString(secureBuffer));

        secureBuffer.readInt();
        secureBuffer.readUnsignedShort();
        secureBuffer.readUnsignedByte();
        secureBuffer.readUnsignedByte();
        secureBuffer.readInt();
        secureBuffer.readUnsignedShort();
        secureBuffer.readUnsignedShort();
        secureBuffer.readInt();

        return message;
    }

    /**
     *
     * @param codec
     * @param outgoingPackets
     */
    private void registerMessageEncoders(MessageCodec codec, PacketMetaList outgoingPackets) {
        codec.registerMessageEncoder(UsernameSuggestionMessage.class, this::encodeUsernameSuggestionMessage);
        codec.registerMessageEncoder(ProfileMessage.class, this::encodeProfileMessage);
    }

    /**
     *
     * @param allocator
     * @param packetMetaList
     * @param message
     * @return
     */
    private Packet encodeUsernameSuggestionMessage(ByteBufAllocator allocator, PacketMetaList packetMetaList, UsernameSuggestionMessage message) {
        List<String> usernames = message.getSuggestedUsernames();
        PacketBuilder builder = PacketBuilder.create(21, "suggest_usernames", usernames.size() * 8 + 1, allocator);
        builder.writeByte(usernames.size());
        for(String username : usernames) {
            builder.writeLong(Base37.encode(username));
        }
        return builder.build();
    }
}
