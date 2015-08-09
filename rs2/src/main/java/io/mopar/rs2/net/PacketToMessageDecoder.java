package io.mopar.rs2.net;

import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.net.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketToMessageDecoder extends MessageToMessageDecoder<Packet> {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(PacketToMessageDecoder.class);

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
        try {
            out.add(codec.decodeMessage(packet));

            // Release the buffer
            ByteBuf buf = packet.getBuffer();
            buf.release();
        } catch (Exception ex) {
            logger.error("Uncaught exception encountered while decoding packet to message", ex);
            throw ex;
        }
    }
}
