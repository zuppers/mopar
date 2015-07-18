package io.mopar.rs2.net;

import io.mopar.rs2.security.ISAACCipher;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.MalformedPacketException;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketDecoder extends ByteToMessageDecoder {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

    /**
     * The maximum allowed packet length.
     */
    private static final int MAXIMUM_LENGTH = 0x3fff;

    /**
     * Internal decoder states.
     */
    enum State { READ_OPCODE, READ_LENGTH, READ_PAYLOAD }

    /**
     * The internal decode state.
     */
    private State state = State.READ_OPCODE;

    /**
     * The meta list.
     */
    private PacketMetaList incomingPackets;

    /**
     * The cipher for packet ids, when not <code>null</code> the cipher is used to decode packet ids from
     * the client.
     */
    private ISAACCipher cipher;

    /**
     * The currently being decoded packet id.
     */
    private int id;

    /**
     * The currently being decoded packet length.
     */
    private int length;

    /**
     * Constructs a new {@link PacketDecoder};
     *
     * @param incomingPackets The incoming packet meta data information.
     */
    public PacketDecoder(PacketMetaList incomingPackets) {
        this.incomingPackets = incomingPackets;
    }

    /**
     *
     * @param keys
     */
    public void initCipher(int[] keys) {
        cipher = new ISAACCipher(keys);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        while (buf.isReadable()) {
            if (State.READ_OPCODE == state) {
                id = (buf.readByte() - (cipher != null ? cipher.getNextValue() : 0)) & 0xff;
                if(!incomingPackets.contains(id)) {
                    logger.warn("Unregistered packet " + id);
                    throw new MalformedPacketException("Unregistered packet " + id);
                }
                length = incomingPackets.getLength(id);
                state = State.READ_LENGTH;
            }

            if (State.READ_LENGTH == state) {
                if(PacketMetaData.VAR_BYTE_LENGTH == length) {
                    if(!buf.isReadable(1)) {
                        break;
                    }
                    length = buf.readUnsignedByte();
                } else if(PacketMetaData.VAR_SHORT_LENGTH == length) {
                    if(!buf.isReadable(2)) {
                        break;
                    }
                    length = buf.readUnsignedShort();
                }

                if(length >= MAXIMUM_LENGTH) {
                    throw new MalformedPacketException("Packet exceeds maximum length " + id + ", " + length);
                }

                state = State.READ_PAYLOAD;
            }

            if (State.READ_PAYLOAD == state) {
                if(!buf.isReadable(length)) {
                    break;
                }

                list.add(new Packet(incomingPackets.get(id), buf.readBytes(length)));
                state = State.READ_OPCODE;
            }
        }
    }
}
