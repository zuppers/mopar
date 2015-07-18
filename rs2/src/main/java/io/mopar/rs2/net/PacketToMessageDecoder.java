package io.mopar.rs2.net;

import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.net.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketToMessageDecoder extends MessageToMessageDecoder<Packet> {

    /**
     * The message codec.
     */
    private MessageCodec codec;

    /**
     * Constructs a new {@link PacketToMessageDecoder};
     *
     * @param codec The message codec.
     */
    public PacketToMessageDecoder(MessageCodec codec) {
        this.codec = codec;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        out.add(codec.decodeMessage(packet));
    }
}
