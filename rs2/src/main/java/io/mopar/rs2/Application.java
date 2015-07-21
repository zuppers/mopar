package io.mopar.rs2;

import io.mopar.cache.FileSystem;
import io.mopar.core.asset.AssetLoader;
import io.mopar.core.asset.StaticFileAssetLoader;
import io.mopar.core.msg.Message;
import io.mopar.file.DiskFileProvider;
import io.mopar.game.GameService;
import io.mopar.game.GameServiceBuilder;
import io.mopar.login.LoginService;
import io.mopar.rs2.game.GameApplicationService;
import io.mopar.rs2.login.LoginApplicationService;
import io.mopar.rs2.msg.*;
import io.mopar.rs2.net.ServerChannelInitializer;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.mopar.rs2.file.FileApplicationService;
import io.mopar.file.FileServiceBuilder;
import io.mopar.rs2.world.WorldApplicationService;
import io.mopar.world.WorldService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class Application {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * The services.
     */
    private Map<Class<? extends ApplicationService>, ApplicationService> services = new HashMap<>();

    /**
     * The default message dispatcher.
     */
    private MessageDispatcher dispatcher = new MessageDispatcher();

    /**
     * The codec initializers.
     */
    private List<MessageCodecInitializer> codecInitializers = new ArrayList<>();

    /**
     * The message codec.
     */
    private MessageCodec messageCodec = new MessageCodec();

    /**
     * The incoming packet meta data.
     */
    private PacketMetaList incomingPackets = new PacketMetaList();

    /**
     * The outgoing packet meta data.
     */
    private PacketMetaList outgoingPackets = new PacketMetaList();

    /**
     * The asset loader.
     */
    private AssetLoader assetLoader = AssetLoader.DUMMY;

    /**
     * Constructs a new {@link Application};
     */
    public Application() {}

    /**
     * Sets up the server.
     */
    public void setup() {

        // Start each of the services
        services.forEach(($_, service) -> service.start(this));

        // Initialize the message codec
        codecInitializers.forEach(initializer -> initializer.initialize(messageCodec, incomingPackets, outgoingPackets));
    }

    /**
     * Binds a service.
     *
     * @param service The service to bind.
     */
    public void use(ApplicationService service) {
        services.put(service.getClass(), service);
    }

    /**
     * Registers a message codec initializer.
     *
     * @param initializer The codec initializer.
     */
    public void register(MessageCodecInitializer initializer) { codecInitializers.add(initializer); }

    /**
     * Gets a bound service.
     *
     * @param serviceClass The service class.
     * @param <T> The generic service type.
     * @return The registered service for the service class or <code>null</code> if there is no registered service
     *          for the service class.
     */
    public <T extends ApplicationService> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    /**
     * Helper method; registers a message handler.
     *
     * @param type The message type.
     * @param handler The message handler.
     * @param <T> The generic message type.
     */
    public <T extends Message> void registerMessageHandler(Class<T> type, MessageHandler<T> handler) {
        dispatcher.registerHandler(type, handler);
    }

    /**
     * Gets the default message dispatcher.
     *
     * @return The dispatcher.
     */
    public MessageDispatcher getDefaultDispatcher() { return dispatcher; }

    /**
     * Gets the message codec.
     *
     * @return The message codec.
     */
    public MessageCodec getMessageCodec() {
        return messageCodec;
    }

    /**
     * Gets the incoming packet meta data.
     *
     * @return The incoming packets.
     */
    public PacketMetaList getIncomingPackets() {
        return incomingPackets;
    }

    /**
     * Gets the outgoing packet meta data.
     *
     * @return THe outgoing packets.
     */
    public PacketMetaList getOutgoingPackets() { return outgoingPackets; }

    /**
     * Sets the asset loader.
     *
     * @param assetLoader The asset loader.
     */
    public void setAssetLoader(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    /**
     * Gets the asset loader.
     *
     * @return The asset loader.
     */
    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    /**
     * Starts the server.
     *
     * @param port The local port to bind.
     * @throws IOException An I/O exception was encountered while binding the socket.
     */
    public void start(int port) throws IOException {
        start(new InetSocketAddress(port));
    }

    /**
     * Starts the server.
     *
     * @param address The socket address to bind.
     * @throws IOException An I/O exception was encountered while binding the socket.
     */
    public void start(SocketAddress address) throws IOException {
        setup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup parentGroup = new NioEventLoopGroup();
            NioEventLoopGroup childGroup = new NioEventLoopGroup();
            bootstrap.channel(NioServerSocketChannel.class)
                    .group(parentGroup, childGroup)
                    .childHandler(new ServerChannelInitializer(this))
                    .bind(address)
                    .sync();
            logger.info("Server started on " + address);
        } catch (InterruptedException ex) {
            throw new IOException("Failed to bind the server address " + address, ex);
        }
    }

    public static void main(String... args) throws IOException {
        FileSystem fileSystem = FileSystem.create(Paths.get("file/data"), 29);

        Application app = new Application();
        app.setAssetLoader(new StaticFileAssetLoader("rs2/asset"));

        app.register(new DefaultCodecInitializer());

        app.use(new FileApplicationService(FileServiceBuilder.create()
                .provider(new DiskFileProvider(fileSystem))
                .build()));

        app.use(new WorldApplicationService(new WorldService()));
        app.use(new LoginApplicationService(new LoginService()));

        GameService gameService = GameServiceBuilder.create()
                .assetLoader(new StaticFileAssetLoader("game/asset"))
                .build();
        gameService.eval("require 'bootstrap'", (res) -> {});
        app.use(new GameApplicationService(gameService));
        app.start(40001);
    }
}
