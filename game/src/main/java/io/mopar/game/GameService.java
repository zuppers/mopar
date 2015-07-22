package io.mopar.game;

import io.mopar.core.*;
import io.mopar.core.lua.Coerce;
import io.mopar.core.asset.AssetLoader;
import io.mopar.game.action.ActionBindings;
import io.mopar.core.lua.LuaScriptEngine;
import io.mopar.core.profile.ProfileCodec;
import io.mopar.game.event.*;
import io.mopar.game.event.player.PlayerCommandEvent;
import io.mopar.game.event.player.PlayerCreatedEvent;
import io.mopar.game.event.player.PlayerDisplayUpdateEvent;
import io.mopar.game.lua.*;
import io.mopar.game.model.*;
import io.mopar.game.model.Route.Point;
import io.mopar.game.msg.ChatMessage;
import io.mopar.game.req.*;
import io.mopar.game.res.*;
import io.mopar.game.util.ExecutionTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public class GameService extends Service {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    /**
     * The event handler bindings.
     */
    private EventBindings eventBindings = new EventBindings();

    /**
     * The script engine.
     */
    private LuaScriptEngine scriptEngine = new LuaScriptEngine();

    /**
     * The action bindings.
     */
    private ActionBindings actionBindings = new ActionBindings();

    /**
     * The codec for decoding and encoding player profiles.
     */
    private ProfileCodec profileCodec = new ProfileCodec();

    /**
     * The asset loader, by default we use the dummy asset loader.
     */
    private AssetLoader assetLoader = AssetLoader.DUMMY;

    /**
     * The timer used for monitoring the amount of ticks to run per cycle.
     */
    private ExecutionTimer timer = new ExecutionTimer();

    /**
     * The world.
     */
    private World world = new World();

    /**
     * The world update rate in milliseconds.
     */
    private long delay = 600L;

    /**
     * The cycle rate in milliseconds.
     */
    private long rate = 50L;

    /**
     * The minimum delay between cycles.
     */
    private long min = 5L;

    /**
     * The current loop cycle.
     */
    private int loopCycle = 0;

    /**
     * Constructs a new {@link GameService};
     */
    public GameService() {}

    /**
     * Sets the profile codec.
     *
     * @param profileCodec The profile codec.
     */
    public void setProfileCodec(ProfileCodec profileCodec) {
        this.profileCodec = profileCodec;
    }

    /**
     * Sets the asset loader.
     *
     * @param assetLoader The asset loader.
     */
    public void setAssetLoader(AssetLoader assetLoader) { this.assetLoader = assetLoader; }

    @Override
    public void setup() {
        initScriptEngine();
        registerRequestHandlers();

        // Reset the execution timer
        timer.reset();
    }

    @Override
    public void pulse() {
        int elapsed = timer.sleep(min, rate);
        int step = (int) (delay / rate);
        for(int i = 0; i < elapsed; i++) {
            if((loopCycle++ % step == 0)) {
                world.update();
            }
        }
    }

    @Override
    public void teardown() {

        // Update the world one last time before tearing down the service
        world.update();
    }

    /**
     * Creates a new player.
     *
     * @param callback The response callback.
     */
    public void createPlayer(Callback<NewPlayerResponse> callback) {
        submit(new NewPlayerRequest(), callback);
    }

    /**
     * Removes a player.
     *
     * @param playerId the player id.
     * @param response the response callback.
     */
    public void removePlayer(int playerId, Callback<RemovePlayerResponse> response) {
        submit(new RemovePlayerRequest(playerId), response);
    }

    /**
     * Routes a player.
     *
     * @param playerId the player id.
     * @param route the route.
     * @param callback The response callback.
     */
    public void route(int playerId, Route route, Callback<RoutePlayerResponse> callback) {
        submit(new RoutePlayerRequest(playerId, route), callback);
    }

    /**
     * Updates the players chat.
     *
     * @param playerId The player id.
     * @param message The message.
     * @param callback The response callback.
     */
    public void chat(int playerId, ChatMessage message, Callback<ChatResponse> callback) {
        submit(new PublicChatRequest(playerId, message), callback);
    }

    /**
     * Handles when a player presses a button.
     *
     * @param playerId the player id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     * @param childId the child id.
     * @param option the option.
     * @param callback the response callback.
     */
    public void buttonPressed(int playerId, int widgetId, int componentId, int childId, int option,
                              Callback<ButtonActionResponse> callback) {
        submit(new ButtonActionRequest(playerId, widgetId, componentId, childId, option), callback);
    }

    /**
     *
     * @param playerId
     * @param widgetId
     * @param componentId
     * @param firstSlot
     * @param secondSlot
     * @param mode
     * @param callback
     */
    public void swapItem(int playerId, int widgetId, int componentId, int firstSlot, int secondSlot,
                         int mode, Callback<SwapItemResponse> callback) {
        submit(new SwapItemActionRequest(playerId, widgetId, componentId, firstSlot, secondSlot, mode), callback);
    }

    /**
     *
     * @param playerId
     * @param name
     * @param arguments
     * @param callback
     */
    public void commandEntered(int playerId, String name, String[] arguments, Callback<CommandResponse> callback) {
        submit(new CommandRequest(playerId, name, arguments), callback);
    }

    /**
     * Updates a players display.
     *
     * @param playerId The player id.
     * @param displayMode The display mode.
     * @param callback The response callback.
     */
    public void updateDisplay(int playerId, int displayMode, Callback<UpdateDisplayResponse> callback) {
        submit(new UpdateDisplayRequest(playerId, displayMode), callback);
    }

    /**
     * Helper method; evaluates a script.
     *
     * @param script The script to evaluate.
     * @param callback The response callback.
     */
    public void eval(String script, Callback<EvalScriptResponse> callback) {
        submit(new EvalScriptRequest(script), callback);
    }


    /**
     * Initializer method; initializes the script engine.
     */
    private void initScriptEngine() {
        Coerce.register(Player.class, (plr) -> new PlayerComposite(plr));

        // Register all of the modules
        scriptEngine.put(new ActionsLuaModule(actionBindings));
        scriptEngine.put(new EventLuaModule(eventBindings));
        scriptEngine.put(new WorldLuaModule(world));
        scriptEngine.put(new AssetLuaModule(assetLoader));
        scriptEngine.put(new JsonLuaModule());
    }

    /**
     * Initializer method; binds all of the request handlers.
     */
    private void registerRequestHandlers() {
        // Bind all of the player request handlers
        registerRequestHandler(NewPlayerRequest.class, this::handleNewPlayerRequest);
        registerRequestHandler(RemovePlayerRequest.class, this::handleRemovePlayerRequest);
        registerRequestHandler(RoutePlayerRequest.class, this::handleRoutePlayerRequest);
        registerRequestHandler(PublicChatRequest.class, this::handlePublicChatRequest);
        registerRequestHandler(UpdateDisplayRequest.class, this::handleUpdateDisplayRequest);
        registerRequestHandler(CommandRequest.class, this::handleCommandRequest);

        // Bind all of the action request handlers
        registerRequestHandler(PlayerMenuActionRequest.class, this::handlePlayerActionRequest);
        registerRequestHandler(ButtonActionRequest.class, this::handleButtonActionRequest);
        registerRequestHandler(SwapItemActionRequest.class, this::handleSwapItemRequest);

        // Bind all of the script request handlers
        registerRequestHandler(EvalScriptRequest.class, this::handleEvalScriptRequest);
        registerRequestHandler(LoadScriptRequest.class, this::handleLoadScriptRequest);

        // Bind all of the service request handlers
        registerRequestHandler(GetWorldTimeRequest.class, this::handleGetWorldTimeRequest);
    }

    /**
     * Dispatches an event.
     *
     * @param event the event to dispatch.
     */
    private void dispatchEvent(Event event) {
        eventBindings.handle(event);
    }

    /**
     * Handles a new player request.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleNewPlayerRequest(NewPlayerRequest request, Callback callback) {
        Player player = new Player();

        // If the request includes the player profile, decode the player profile and update the player with
        // the decoded profile.
        if(request.hasProfileData()) {
            try {
                Profile profile = profileCodec.decode(request.getEncoding(), request.getProfileData());

            } catch (IOException ex) {
                logger.error("Failed to decode the provided player profile", ex);
                callback.call(new NewPlayerResponse(NewPlayerResponse.INVALID_PROFILE));
                return;
            }
        }

        // Attempt to add the player to the world, if it is full then we cannot go any further.
        if(!world.addPlayer(player)) {
            callback.call(new NewPlayerResponse(NewPlayerResponse.FULL));
            return;
        }

        // Callback that the request was successful
        NewPlayerResponse response = new NewPlayerResponse(NewPlayerResponse.OK);
        response.setPlayer(player);
        callback.call(response);

        // TODO: Clean this up better
        eventBindings.handle(new PlayerCreatedEvent(player));
    }

    /**
     * Handles a display update request.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleUpdateDisplayRequest(UpdateDisplayRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new UpdateDisplayResponse());
            return;
        }

        int mode = request.getDisplayMode();
        if(mode < 0 || mode >= DisplayMode.FULLSCREEN_HD) {
            callback.call(new UpdateDisplayResponse());
            return;
        }

        if(mode != player.getDisplayMode()) {
            player.setDisplayMode(request.getDisplayMode());
            dispatchEvent(new PlayerDisplayUpdateEvent(player));
        }

        callback.call(new UpdateDisplayResponse());
    }

    /**
     * Handles a remove player request.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleRemovePlayerRequest(RemovePlayerRequest request, Callback callback) {
        if(!world.removePlayer(request.getPlayerId())) {
            callback.call(new RemovePlayerResponse(RemovePlayerResponse.PLAYER_DOES_NOT_EXIST));
            return;
        }

        callback.call(new RemovePlayerResponse(RemovePlayerResponse.OK));
    }

    /**
     * Handles a button action request.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleButtonActionRequest(ButtonActionRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new ButtonActionResponse());
            return;
        }

        if(!actionBindings.callButtonMenuAction(player, request.getWidgetId(), request.getComponentId(), request.getChildId(), request.getOption())) {
            logger.info("No binding for button id: " + request.getWidgetId() + ", comp: " + request.getComponentId() + ", " +
                    "option: " + request.getOption() + (request.getChildId() != -1 ? ", child: " + request.getChildId() : ""));
            callback.call(new ButtonActionResponse());
            return;
        }

        callback.call(new ButtonActionResponse());
    }

    /**
     * Handles a player action request.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handlePlayerActionRequest(PlayerMenuActionRequest request, Callback callback) {

        // Get the action source, if the source does not exist we cannot go any further.
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new MenuActionResponse(MenuActionResponse.SOURCE_DOES_NOT_EXIST));
            return;
        }

        // Get the action target, if the target does not exist we cannot go any further.
        Player target = world.getPlayer(request.getTargetPlayerId());
        if(target == null) {
            callback.call(new MenuActionResponse(MenuActionResponse.TARGET_DOES_NOT_EXIST));
            return;
        }

        // Call the specified action, if the action does not exist alert the callback
        if(!actionBindings.callPlayerMenuAction(player, target, request.getOption())) {
            callback.call(new MenuActionResponse(MenuActionResponse.NO_SUCH_ACTION));
            return;
        }

        // Callback that the request was successful
        callback.call(new MenuActionResponse(MenuActionResponse.OK));
    }

    /**
     * Handles a request to route a player.
     *
     * @param request The request.
     * @param callback The response callback.
     */
    private void handleRoutePlayerRequest(RoutePlayerRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new RoutePlayerResponse(RoutePlayerResponse.PLAYER_DOES_NOT_EXIST));
            return;
        }

        player.clearSteps();

        // TODO: At some point update this since you dont need to interpolate between all the points, so it'd save some
        // TODO: processing power and memory
        Position currentPosition = player.getPosition();
        Route route = request.getRoute();
        for(Point point : route.getPoints()) {
            player.addSteps(point.interpolate(currentPosition));
            currentPosition = new Position(point.getX(), point.getY());
        }

        callback.call(new RoutePlayerResponse(RoutePlayerResponse.OK));
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleSwapItemRequest(SwapItemActionRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new SwapItemResponse());
        }

        if(!actionBindings.callSwapItemAction(player, request.getWidgetId(), request.getComponentId(),
                request.getFirstSlot(), request.getSecondSlot(), request.getMode())) {
            logger.info("No binding for interface swap item, id: " + request.getWidgetId() + ", comp: " + request.getComponentId());
            callback.call(new SwapItemResponse());
            return;
        }

        callback.call(new SwapItemResponse());
    }

    /**
     * Handles a request to publish a public chat message..
     *
     * @param request The request.
     * @param callback The response callback.
     */
    private void handlePublicChatRequest(PublicChatRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new ChatResponse(ChatResponse.PLAYER_DOES_NOT_EXIST));
            return;
        }

        player.setPublicChatMessage(request.getMessage());

        callback.call(new ChatResponse(ChatResponse.OK));
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleCommandRequest(CommandRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new CommandResponse());
            return;
        }

        dispatchEvent(new PlayerCommandEvent(player, request.getName(), request.getArguments()));

        callback.call(new CommandResponse());
    }

    /**
     * Handles a request to evaluate a script.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleEvalScriptRequest(EvalScriptRequest request, Callback callback) {
        try {
            Object result = scriptEngine.eval(request.getScript());

            // Callback that the request was successful and the result of the script
            EvalScriptResponse response = new EvalScriptResponse(EvalScriptResponse.OK);
            response.setResult(result);
            callback.call(response);
        } catch (ScriptException ex) {
            logger.error("Failed to evaluate script", ex);

            // TODO(sinisoul): How to effectively callback the exception?
            callback.call(new EvalScriptResponse(EvalScriptResponse.ERROR));
        }
    }

    /**
     * Handles a request to load a script.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleLoadScriptRequest(LoadScriptRequest request, Callback callback) {
        try {
            scriptEngine.load(request.getNamespace(), request.getScript());
        } catch (ScriptException ex) {
            logger.error("Failed to load script", ex);

            // TODO(sinisoul): How to effectively callback the exception?
            callback.call(new LoadScriptResponse(LoadScriptResponse.ERROR));
        }
    }

    /**
     * Handles a request to get the current world time.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleGetWorldTimeRequest(GetWorldTimeRequest request, Callback callback) {
        callback.call(new GetWorldTimeResponse(world.getTime()));
    }
}