package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;
import io.mopar.rs2.net.packet.MalformedPacketException;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class MessageCodec {

    /**
     * The message decoders.
     */
    private Map<Integer, MessageDecoder> decoders = new HashMap<>();

    /**
     * The message encoders.
     */
    private Map<Class<? extends Message>, MessageEncoder> encoders = new HashMap<>();

    /**
     * Registers a message decoder.
     *
     * @param id The packet id that the decoder will be mapped to.
     * @param decoder The message decoder.
     */
    public void registerMessageDecoder(int id, MessageDecoder decoder) {
        decoders.put(id, decoder);
    }

    /**
     * Registers a message encoder.
     *
     * @param type The message type.
     * @param encoder The message encoder.
     */
    public <T extends Message> void registerMessageEncoder(Class<T> type, MessageEncoder<T> encoder) {
        encoders.put(type, encoder);
    }

    /**
     * Decodes a message from a packet.
     *
     * @param packet The packet to decode the message with.
     * @return The decoded message.
     * @throws NullPointerException If there is no registered message decoder for the given packet.
     */
    public Message decodeMessage(Packet packet) throws MalformedPacketException {
        MessageDecoder decoder = decoders.get(packet.getId());
        if(decoder == null) {
            throw new NullPointerException("Internal error; no decoder registered for packet " + packet.getId());
        }
        return decoder.decode(packet);
    }

    /**
     * Encoders a message to a packet.
     *
     * @param allocator The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param message The message to encode.
     * @return The encoded packet.
     * @throws NullPointerException If there is no registered message encoder for the given message.
     */
    public Packet encodeMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, Message message) {
        MessageEncoder encoder = encoders.get(message.getClass());
        if(encoder == null) {
            throw new NullPointerException("Internal error; no encoder registered for message " + message.getClass().getSimpleName());
        }
        return encoder.encode(allocator, outgoingPackets, message);
    }
}
