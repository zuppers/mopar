package io.mopar.rs2.net;

import io.mopar.rs2.Application;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Hadyn Fitzgerald
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * The application.
     */
    private Application app;

    /**
     * Constructs a new {@link ServerChannelInitializer};
     *
     * @param app The application.
     */
    public ServerChannelInitializer(Application app) {
        this.app = app;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        Session session = new Session(channel);
        session.setDispatcher(app.getDefaultDispatcher());

        channel.pipeline()
                .addLast("packet_decoder", new PacketDecoder(app.getIncomingPackets()))
                .addLast("packet_encoder", new PacketEncoder())
                .addLast("packet_message_decoder", new PacketToMessageDecoder(app.getMessageCodec()))
                .addLast("message_packet_encoder", new MessageToPacketEncoder(app.getOutgoingPackets(), app.getMessageCodec()))
                .addLast("channel_handler", new SessionMessageHandler(session));
    }
}
