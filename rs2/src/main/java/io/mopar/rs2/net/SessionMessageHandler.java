package io.mopar.rs2.net;

import io.mopar.core.msg.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hadyn Fitzgerald
 */
public class SessionMessageHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SessionMessageHandler.class);

    /**
     * The session.
     */
    private Session session;

    /**
     * Constructs a new {@link SessionMessageHandler};
     *
     * @param session The session.
     */
    public SessionMessageHandler(Session session) {
        this.session = session;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        session.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message message) throws Exception {
        session.dispatch(message);
    }
}