package io.mopar.game;

import io.mopar.core.*;
import io.mopar.core.lua.Coerce;
import io.mopar.core.asset.AssetLoader;
import io.mopar.game.action.ActionBindings;
import io.mopar.core.lua.LuaScriptEngine;
import io.mopar.core.profile.ProfileCodec;
import io.mopar.game.lua.*;
import io.mopar.game.model.Position;
import io.mopar.game.model.Route;
import io.mopar.game.model.Route.Point;
import io.mopar.game.model.state.States;
import io.mopar.game.req.*;
import io.mopar.game.res.*;
import io.mopar.game.model.Player;
import io.mopar.game.model.World;
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
        for(int i = 0; i < elapsed; i++) {
            if((loopCycle++ % (delay / rate) == 0)) {
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
     *
     * @param playerId
     * @param response
     */
    public void removePlayer(int playerId, Callback<RemovePlayerResponse> response) {
        submit(new RemovePlayerRequest(playerId), response);
    }

    /**
     *
     * @param playerId
     * @param route
     */
    public void route(int playerId, Route route, Callback callback) {
        submit(new RoutePlayerRequest(playerId, route), callback);
    }

    /**
     *
     * @param playerId
     * @param displayMode
     * @param callback
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
        scriptEngine.put(new WorldLuaModule(world));
        scriptEngine.put(new AssetLuaModule(assetLoader));
        scriptEngine.put(new JsonLuaModule());
    }

    /**
     * Initializer method; binds all of the request handlers.
     */
    private void registerRequestHandlers() {
        // Bind all of the create entity request handlers
        registerRequestHandler(NewPlayerRequest.class, this::handleNewPlayerRequest);
        registerRequestHandler(RemovePlayerRequest.class, this::handleRemovePlayerRequest);
        registerRequestHandler(RoutePlayerRequest.class, this::handleRoutePlayerRequest);
        registerRequestHandler(UpdateDisplayRequest.class, this::handleUpdateDisplayRequest);

        // Bind all of the menu action request handlers
        registerRequestHandler(PlayerMenuActionRequest.class, this::handlePlayerActionRequest);

        // Bind all of the script request handlers
        registerRequestHandler(EvalScriptRequest.class, this::handleEvalScriptRequest);

        // Bind all of the service request handlers
        registerRequestHandler(GetWorldTimeRequest.class, this::handleGetWorldTimeRequest);
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

        player.queueState(States.FRESH);

        // Callback that the request was successful
        NewPlayerResponse response = new NewPlayerResponse(NewPlayerResponse.OK);
        response.setPlayer(player);
        callback.call(response);
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

        // TODO: Is queuing a state for this the best way to do this?
        player.setDisplayMode(request.getDisplayMode());
        player.queueState(States.DISPLAY_MODE_UPDATED);

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

        Position currentPosition = player.getPosition();
        Route route = request.getRoute();
        for(Point point : route.getPoints()) {
            player.addSteps(point.interpolate(currentPosition));
            currentPosition = new Position(point.getX(), point.getY());
        }

        callback.call(new RoutePlayerResponse(RoutePlayerResponse.OK));
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
