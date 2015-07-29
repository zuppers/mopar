package io.mopar.rs2.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.mopar.account.res.LoginResponse;
import io.mopar.core.asset.AssetLoader;
import io.mopar.game.GameService;
import io.mopar.game.model.Position;
import io.mopar.game.msg.*;
import io.mopar.game.res.NewPlayerResponse;
import io.mopar.rs2.Application;
import io.mopar.rs2.ApplicationService;
import io.mopar.rs2.account.AccountApplicationService;
import io.mopar.rs2.file.FileSessionContext;
import io.mopar.rs2.msg.game.InterfaceItemOptionMessage;
import io.mopar.rs2.msg.StatusMessage;
import io.mopar.rs2.msg.game.*;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

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
    private AccountApplicationService accountService;

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

        AssetLoader assetLoader = app.getAssetLoader();
        try {
            InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(assetLoader.load("game/incoming-packets.json")));
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(reader);
            app.getIncomingPackets().parse(element.getAsJsonArray());
        } catch (URISyntaxException | IOException ex) {
            logger.error("Failed to load incoming packets", ex);
        }

        // Register the message codec initializer
        app.register(new GameMessageCodecInitializer(table));

        // Register the incoming and outgoing packets for the service
        registerOutgoingPackets(app.getOutgoingPackets());

        registerMessageHandler(RouteMessage.class, this::handleRouteMessage);
        registerMessageHandler(ScreenInfoMessage.class, this::handleScreenInfoMessage);
        registerMessageHandler(ChatMessage.class, this::handleChatMessage);
        registerMessageHandler(ButtonOptionMessage.class, this::handleButtonOptionMessage);
        registerMessageHandler(CommandMessage.class, this::handleCommandMessage);
        registerMessageHandler(SessionClosedMessage.class, this::handleSessionClosed);
        registerMessageHandler(SwapItemMessage.class, this::handleSwapItemMessage);
        registerMessageHandler(ItemOptionMessage.class, this::handleItemOptionMessage);
        registerMessageHandler(InterfaceItemOptionMessage.class, this::handleInterfaceItemOptionMessage);

        app.registerMessageHandler(LoginStatusCheck.class, this::handleLoginStatusCheck);
        app.registerMessageHandler(LoginRequestMessage.class, this::handleLoginRequest);

        accountService = app.getService(AccountApplicationService.class);
    }

    /**
     * Registers the outgoing packets.
     *
     * @param outgoingPackets The outgoing packet list.
     */
    private void registerOutgoingPackets(PacketMetaList outgoingPackets) {
        outgoingPackets.add(new PacketMetaData(4, "song", 2));
        outgoingPackets.add(new PacketMetaData(21, "set_interface_hidden", 7));
        outgoingPackets.add(new PacketMetaData(22, "update_inventory", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(32, "npc_update", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(38, "update_skill", 6));
        outgoingPackets.add(new PacketMetaData(60, "variable_b", 3));
        outgoingPackets.add(new PacketMetaData(70, "print", PacketMetaData.VAR_BYTE_LENGTH));
        outgoingPackets.add(new PacketMetaData(105, "refresh_inventory", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(145, "set_root_interface", 5));
        outgoingPackets.add(new PacketMetaData(155, "set_interface", 9));
        outgoingPackets.add(new PacketMetaData(162, "rebuild_scene", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(165, "access_options", 14));
        outgoingPackets.add(new PacketMetaData(171, "interface_text", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(225, "player_update", PacketMetaData.VAR_SHORT_LENGTH));
        outgoingPackets.add(new PacketMetaData(226, "variable_i", 6 ));
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
        accountService.login(request.getUsername(), request.getPassword(), res -> handleLoginResponse(session, request, res));
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleRouteMessage(Session session, RouteMessage message) {
        PlayerSessionContext ctx = session.get(PlayerSessionContext.class);
        service.handleRoute(ctx.getPlayerId(), message.getRoute(), (res) -> {});
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleScreenInfoMessage(Session session, ScreenInfoMessage message) {
        service.updatePlayerDisplay(session.get(PlayerSessionContext.class).getPlayerId(), message.getDisplayMode(), (res) -> {
        });
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleChatMessage(Session session, ChatMessage message) {
        service.handlePublicChatMessage(session.get(PlayerSessionContext.class).getPlayerId(), message, (res) -> {
        });
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleButtonOptionMessage(Session session, ButtonOptionMessage message) {
        service.handleButtonMenuAction(session.get(PlayerSessionContext.class).getPlayerId(), message.getWidgetId(), message
                .getComponentId(), message.getChildId(), message.getOption(), (res) -> {
        });
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleItemOptionMessage(Session session, ItemOptionMessage message) {
        service.handleItemMenuAction(session.get(PlayerSessionContext.class).getPlayerId(), message.getWidgetId(),
                message.getComponentId(), message.getItemId(), message.getSlot(), message.getOption(), (res) -> {
                });
    }

    private void handleInterfaceItemOptionMessage(Session session, InterfaceItemOptionMessage message) {
        service.handleInterfaceItemMenuAction(session.get(PlayerSessionContext.class).getPlayerId(), message.getWidgetId(),
                message.getComponentId(), message.getItemId(), message.getSlot(), message.getOption(), (res) -> {
                });
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleCommandMessage(Session session, CommandMessage message) {
        service.handleCommandAction(session.get(PlayerSessionContext.class).getPlayerId(), message.getName(),
                message.getArguments(), (res) -> {
                });
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleSwapItemMessage(Session session, SwapItemMessage message) {
        service.handleItemSwapAction(session.get(PlayerSessionContext.class).getPlayerId(), message.getWidgetId(),
                message.getComponentId(), message.getFirstSlot(), message.getSecondSlot(), message.getMode(), (res) -> {
                });
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
        switch (response.getStatus()) {
            // Ignore this, just to acknowledge that the request was accepted
            case LoginResponse.ACCEPTED:
                return;

            // Notify the client that the provided credentials were invalid
            case LoginResponse.INVALID_USER_OR_PASS:
            case LoginResponse.INTERNAL_ERROR:
                session.writeAndFlush(StatusMessage.INVALID_USER_OR_PASS);
                session.close();
                return;

        }

        service.createPlayer(request.getUsername(), response.getProfile(), res -> {
            switch (res.getStatus()) {
                case NewPlayerResponse.FULL:
                    session.writeAndFlush(StatusMessage.FULL);
                    session.close();
                    return;
                case NewPlayerResponse.ALREADY_ONLINE:
                    session.writeAndFlush(StatusMessage.ALREADY_ONLINE);
                    session.close();
                    return;
            }

            // TODO(sinisoul: This most likely will not fix the problem. Hurr.
            if(session.get(FileSessionContext.class) != null) {
                logger.info("Session attempted to register to file and game service");
                session.close();
                return;
            }

            // TODO: Clean this up properly
            session.attach(PlayerSessionContext.class, new PlayerSessionContext(res.getPlayer()));
            session.setDispatcher(dispatcher);

            // TODO(sinisoul): Flesh this out more instead of using it as a testing platform
            session.writeAndFlush(new ProfileMessage(res.getPlayer().getId())).awaitUninterruptibly();

            res.getPlayer().addMessageListener(msg -> session.writeAndFlush(msg));

            // Initialize the packet encoder and decoder ciphers
            int[] cipherKeys = request.getCipherKeys();
            session.pipeline().get(PacketDecoder.class).initCipher(cipherKeys);
            for (int i = 0; i < cipherKeys.length; i++) {
                cipherKeys[i] += 50;
            }
            session.pipeline().get(PacketEncoder.class).initCipher(cipherKeys);

            res.getPlayer().setDisplayMode(request.getDisplayMode());
            res.getPlayer().rebuildScene();
            res.getPlayer().refreshSkills();

            // varbit - 4393 : Loop
            int[] configs = new int[]{
                    20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1104, 1136, 1180, 1202
            };
            for(int config : configs) {
                res.getPlayer().send(new SetVariableMessage(config, 0xffffffff));
            }
            // option 1
            res.getPlayer().send(new AccessOptionMessage(187, 1, 0, 646, 0b10));

            res.getPlayer().setAppearanceUpdated(true);
        });
    }
}
