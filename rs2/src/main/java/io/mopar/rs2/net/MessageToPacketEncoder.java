package io.mopar.rs2.net;

import io.mopar.core.msg.Message;
import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class MessageToPacketEncoder extends MessageToMessageEncoder<Message> {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageToPacketEncoder.class);

    /**
     * The outgoing packets.
     */
    private PacketMetaList outgoingPackets;

    /**
     * The message codec.
     */
    private MessageCodec codec;

    /**
     * Constructs a new {@link MessageToPacketEncoder};
     *
     * @param outgoingPackets The outgoing packets.
     * @param codec The message codec.
     */
    public MessageToPacketEncoder(PacketMetaList outgoingPackets, MessageCodec codec) {
        this.outgoingPackets = outgoingPackets;
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) throws Exception {
        try {
            out.add(codec.encodeMessage(ctx.alloc(), outgoingPackets, message));
        } catch (Throwable t) {
            logger.error("Uncaught exception encountered while encoding message", t);
        }
    }
}