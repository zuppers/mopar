package io.mopar.rs2.net;

import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.security.ISAACCipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    /**
     * The cipher.
     */
    private ISAACCipher cipher;

    /**
     * Constructs a new {@link PacketEncoder};
     */
    public PacketEncoder() {}

    /**
     * Initializes the cipher.
     *
     * @param keys The cipher keys.
     */
    public void initCipher(int[] keys) {
        cipher = new ISAACCipher(keys);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
        buf.writeByte(packet.getId() + (cipher != null ? cipher.getNextValue() : 0));
        ByteBuf buffer = packet.getBuffer();
        switch (packet.getLength()) {
            case PacketMetaData.VAR_BYTE_LENGTH:
                buf.writeByte(buffer.writerIndex());
                break;
            case PacketMetaData.VAR_SHORT_LENGTH:
                buf.writeShort(buffer.writerIndex());
                break;
        }
        buf.writeBytes(buffer);
    }
}
