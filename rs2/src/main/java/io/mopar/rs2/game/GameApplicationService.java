package io.mopar.rs2.game;

import io.mopar.game.GameService;
import io.mopar.game.model.Position;
import io.mopar.game.msg.RebuildSceneMessage;
import io.mopar.game.msg.SetInterfaceMessage;
import io.mopar.game.msg.SetRootInterfaceMessage;
import io.mopar.login.res.LoginResponse;
import io.mopar.rs2.Application;
import io.mopar.rs2.ApplicationService;
import io.mopar.rs2.login.LoginApplicationService;
import io.mopar.rs2.msg.StatusMessage;
import io.mopar.rs2.msg.game.RouteMessage;
import io.mopar.rs2.msg.login.LoginRequestMessage;
import io.mopar.rs2.msg.login.LoginStatusCheck;
import io.mopar.rs2.msg.login.ProfileMessage;
import io.mopar.rs2.net.PacketDecoder;
import io.mopar.rs2.net.PacketEncoder;
import io.mopar.rs2.net.Session;
import io.mopar.rs2.net.SessionClosedMessage;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.net.packet.PacketMetaList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hadyn Fitzgerald
 */
public class GameApplicationService extends ApplicationService<GameService> {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(GameApplicationService.class);

    /**
     * The login service.
     */
    private LoginApplicationService loginService;

    /**
     * Constructs a new {@link GameApplicationService};
     *
     * @param service The service.
     */
    public GameApplicationService(GameService service) {
        super(service);
    }

    /**
     * Initializes the service.
     *
     * @param app The application.
     */
    @Override
    public void setup(Application app) {

        LandscapeKeyTable table = new LandscapeKeyTable();
        try {
            table.parse(app.getAssetLoader().load("game/landscape-keys.dat"));
        } catch (Exception ex) {
            logger.error("Failed to load the landscape key table", ex);
        }

        // Register the message codec initializer
        app.register(new GameMessageCodecInitializer(table));

        // Register the incoming and outgoing packets for the service
        registerIncomingPackets(app.getIncomingPackets());
        registerOutgoingPackets(app.getOutgoingPackets());

        registerMessageHandler(RouteMessage.class, this::handleRouteMessage);
        registerMessageHandler(SessionClosedMessage.class, this::handleSessionClosed);

        app.registerMessageHandler(LoginStatusCheck.class, this::handleLoginStatusCheck);
        app.registerMessageHandler(LoginRequestMessage.class, this::handleLoginRequest);

        loginService = app.getService(LoginApplicationService.class);
    }

    /**
     * Registers the incoming packets.
     *
     * @param incomingPackets The incoming packet list.
     */
    private void registerIncomingPackets(PacketMetaList incomingPackets) {
        incomingPackets.add(new PacketMetaData(20, "rebuilt_scene", 4));
        incomingPackets.add(new PacketMetaData(21, "camera_moved", 4));
        incomingPackets.add(new PacketMetaData(22, "focus_changed", 1));
        incomingPackets.add(new PacketMetaData(75, "click", 6));
        incomingPackets.add(new PacketMetaData(93, "heartbeat", 0));
        incomingPackets.add(new PacketMetaData(110, "load_scene", 0));
        incomingPackets.add(new PacketMetaData(177, "packet_check", 2));
        incomingPackets.add(new PacketMetaData(215, "route_ground", PacketMetaData.VAR_BYTE_LENGTH));
        incomingPackets.add(new PacketMetaData(243, "screen_info", 6));
    }

    /**
     * Registers the outgoing packets.
     *
     * @param outgoingPackets The outgoing packet list.
     */
    private void registerOutgoingPackets(PacketMetaList outgoingPackets) {
        outgoingPackets.add(new PacketMetaData(70, "print", PacketMetaData.VAR_BYTE_LENGTH));
        outgoingPackets.add(new PacketMetaData(145, "set_root_interface", 5));
        outgoingPackets.add(new PacketMetaData(155, "set_interface", 9));
        outgoingPackets.add(new PacketMetaData(162, "rebuild_scene", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(225, "player_update", PacketMetaData.VAR_SHORT_LENGTH));
    }

    /**
     * Handles a login status check message.
     *
     * @param session The session that the message was received from.
     * @param message The message.
     */
    private void handleLoginStatusCheck(Session session, LoginStatusCheck message) {
        // TODO(sinisoul): Check if the login service is active and is not full
        session.writeAndFlush(StatusMessage.OK);
    }

    /**
     * Handles a login request message.
     *
     * @param session The session the message was received from.
     * @param request The message.
     */
    private void handleLoginRequest(Session session, LoginRequestMessage request) {
        // TODO(sinisoul): Where do I put this? Do I handle it from the application service or?
        loginService.getService().login(res -> handleLoginResponse(session, request, res));
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleRouteMessage(Session session, RouteMessage message) {
        PlayerSessionContext ctx = session.get(PlayerSessionContext.class);
        service.route(ctx.getPlayer().getId(), message.getRoute(), (res) -> {});
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleSessionClosed(Session session, SessionClosedMessage message) {
        service.removePlayer(session.get(PlayerSessionContext.class).getPlayerId(), (res) -> {});
    }

    /**
     * Handles a login request response.
     *
     * @param session The session.
     * @param request The request.
     * @param response The response.
     */
    private void handleLoginResponse(Session session, LoginRequestMessage request, LoginResponse response) {
        service.createPlayer(res -> {
            // TODO: Clean this up properly
            session.attach(PlayerSessionContext.class, new PlayerSessionContext(res.getPlayer()));
            session.setDispatcher(dispatcher);

            // TODO(sinisoul): Flesh this out more instead of using it as a testing platform
            session.writeAndFlush(new ProfileMessage()).awaitUninterruptibly();

            res.getPlayer().addMessageListener(msg -> session.writeAndFlush(msg));

            // Initialize the packet encoder and decoder ciphers
            int[] cipherKeys = request.getCipherKeys();
            session.pipeline().get(PacketDecoder.class).initCipher(cipherKeys);
            for (int i = 0; i < cipherKeys.length; i++) {
                cipherKeys[i] += 50;
            }
            session.pipeline().get(PacketEncoder.class).initCipher(cipherKeys);

            Position position = new Position(3200, 3200);
            res.getPlayer().setPosition(position);

            res.getPlayer().rebuildScene();
            session.write(new SetRootInterfaceMessage(548));
            session.write(new SetInterfaceMessage(548, 75, 752, 1));
            session.write(new SetInterfaceMessage(548, 14, 751, 1));
            session.write(new SetInterfaceMessage(752, 8, 137, 1));
            session.write(new SetInterfaceMessage(548, 10, 754, 1));
            session.flush();
        });
    }
}
