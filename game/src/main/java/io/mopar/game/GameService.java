package io.mopar.game;

import io.mopar.account.*;
import io.mopar.core.*;
import io.mopar.core.lua.Coerce;
import io.mopar.core.asset.AssetLoader;
import io.mopar.game.action.ActionBindings;
import io.mopar.core.lua.LuaScriptEngine;
import io.mopar.game.event.*;
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
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Arrays;

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
     *
     */
    private ProfileSerializer profileSerializer;

    /**
     * Constructs a new {@link GameService};
     */
    public GameService() {}

    /**
     * Sets the asset loader.
     *
     * @param assetLoader The asset loader.
     */
    public void setAssetLoader(AssetLoader assetLoader) { this.assetLoader = assetLoader; }

    /**
     *
     * @param serializer
     */
    public void setProfileSerializer(ProfileSerializer serializer) {
        this.profileSerializer = serializer;
    }

    /**
     *
     * @param cls
     * @param handler
     * @param <T>
     */
    public <T extends Event> void registerEventHandler(Class<T> cls, EventHandler<T> handler) {
        eventBindings.add(cls, handler);
    }

    @Override
    public void setup() {
        initScriptEngine();
        registerRequestHandlers();

        try {
            GameObjectConfig.init(new ByteArrayInputStream(assetLoader.load("objs.dat")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // TODO: Better way of doing this?
        world.getRegions().setLoader(new AssetRegionLoader(assetLoader));

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

                // Every 5 minutes automatically save all of the players
                if(world.getTime() % 500 == 0) {
                    if(world.getAmountPlayers() > 0) {
                        logger.info("Automatically saving all active player profiles");
                        saveProfiles();
                    }
                }
            }
        }
    }

    @Override
    public void teardown() {
        // Update the world one last time before tearing down the service
        world.update();

        // Save all of the profiles
        // TODO: Fix this
        saveProfiles();
    }

    /**
     *
     */
    public void saveProfiles() {
        for (Player player : world.getPlayers()) {
            profileSerializer.save(player.toProfile(), (res) -> {});
        }
    }

    /**
     * Creates a new player.
     *
     * @param uid
     * @param profile
     * @param callback The response callback.
     */
    public void createPlayer(long uid, Profile profile, Callback<NewPlayerResponse> callback) {
        NewPlayerRequest request = new NewPlayerRequest(uid);
        request.setProfile(profile);
        submit(request, callback);
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
     *
     * @param playerId the player id.
     * @param route the route.
     * @param callback The response callback.
     */
    public void handleRoute(int playerId, Route route, Callback<RoutePlayerResponse> callback) {
        submit(new RoutePlayerRequest(playerId, route), callback);
    }

    /**
     *
     * @param playerId The player id.
     * @param message The message.
     * @param callback The response callback.
     */
    public void handlePublicChatMessage(int playerId, ChatMessage message, Callback<ChatResponse> callback) {
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
    public void handleButtonMenuAction(int playerId, int widgetId, int componentId, int childId, int option,
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
    public void handleItemSwapAction(int playerId, int widgetId, int componentId, int firstSlot, int secondSlot,
                                     int mode, Callback<SwapItemResponse> callback) {
        submit(new SwitchItemActionRequest(playerId, widgetId, componentId, firstSlot, secondSlot, mode), callback);
    }

    /**
     * Submits a item menu action.
     *
     * @param playerId the player id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     * @param itemId the item id.
     * @param slot the slot in the inventory.
     * @param callback the response callback.
     */
    public void handleItemMenuAction(int playerId, int widgetId, int componentId, int itemId, int slot, int option, Callback<ItemMenuActionResponse> callback) {
        submit(new ItemMenuActionRequest(playerId, widgetId, componentId, itemId, slot, option), callback);
    }

    /**
     *
     * @param playerId
     * @param command
     * @param arguments
     * @param callback the response callback.
     */
    public void handleCommandAction(int playerId, String command, String[] arguments, Callback<CommandResponse> callback) {
        submit(new CommandActionRequest(playerId, command, arguments), callback);
    }

    /**
     * Updates a players display.
     *
     * @param playerId The player id.
     * @param displayMode The display mode.
     * @param callback The response callback.
     */
    public void updatePlayerDisplay(int playerId, int displayMode, Callback<UpdateDisplayResponse> callback) {
        submit(new UpdateDisplayRequest(playerId, displayMode), callback);
    }

    /**
     * Evaluates a script.
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
        Coerce.register(Item.class, (itm) -> new ItemComposite(itm));

        // Register all of the modules
        scriptEngine.put(new ActionsLuaModule(actionBindings));
        scriptEngine.put(new ServiceModule(this));
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

        // Bind all of the action request handlers
        registerRequestHandler(PlayerMenuActionRequest.class, this::handlePlayerActionRequest);
        registerRequestHandler(ButtonActionRequest.class, this::handleButtonActionRequest);
        registerRequestHandler(SwitchItemActionRequest.class, this::handleSwitchItemRequest);
        registerRequestHandler(ItemMenuActionRequest.class, this::handleItemMenuActionRequest);
        registerRequestHandler(CommandActionRequest.class, this::handleCommandRequest);
        registerRequestHandler(InterfaceItemMenuActionRequest.class, this::handleInterfaceItemMenuActionRequest);

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
        player.setUsername(request.getUsername());

        if(world.hasPlayer(player.getUid())) {
            callback.call(new NewPlayerResponse(NewPlayerResponse.ALREADY_ONLINE));
            return;
        }

        // Attempt to add the player to the world, if it is full then we cannot go any further.
        if(!world.addPlayer(player)) {
            callback.call(new NewPlayerResponse(NewPlayerResponse.FULL));
            return;
        }

        if(request.hasProfile()) {
            Profile profile = request.getProfile();
            player.setPosition(new Position(profile.getX(), profile.getY(), profile.getPlane()));

            for(SkillModel skill : profile.getSkills()) {
                SkillSet skills = player.getSkills();
                skills.setStat(skill.getId(), skill.getStat());
                skills.setExperience(skill.getId(), skill.getExperience());
            }

            for(InventoryModel inventory : profile.getInventories()) {
                Inventory inv = player.getInventory(inventory.getId());
                for(ItemModel item : inventory.getItems()) {
                    inv.set(item.getSlot(), new Item(item.getId(), item.getAmount()));
                }
            }
        }

        // Callback that the request was successful
        NewPlayerResponse response = new NewPlayerResponse(NewPlayerResponse.OK);
        response.setPlayer(player);
        callback.call(response);

        // Dispatch an event signaling that the player was created
        dispatchEvent(new PlayerCreatedEvent(player));
    }

    /**
     * Handles a remove player request.
     *
     * @param request The request.
     * @param callback The callback.
     */
    private void handleRemovePlayerRequest(RemovePlayerRequest request, Callback callback) {
        if(!world.playerExists(request.getPlayerId())) {
            callback.call(new RemovePlayerResponse(RemovePlayerResponse.PLAYER_DOES_NOT_EXIST));
            return;
        }

        Player player = world.getPlayer(request.getPlayerId());
        profileSerializer.save(player.toProfile(), (res) -> {});

        world.removePlayer(request.getPlayerId());
        callback.call(new RemovePlayerResponse(RemovePlayerResponse.OK));
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

        if(!actionBindings.callButtonMenuAction(player, request.getWidgetId(), request.getComponentId(),
                request.getChildId(), request.getOption())) {
            logger.info("No binding for button menu action, id: " + request.getWidgetId() + ", comp: " + request.getComponentId() + ", " +
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
    private void handleSwitchItemRequest(SwitchItemActionRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new SwapItemResponse());
            return;
        }

        if(!actionBindings.callSwitchItemAction(player, request.getWidgetId(), request.getComponentId(),
                request.getFirstSlot(), request.getSecondSlot(), request.getMode())) {
            logger.info("No binding for interface switch item, id: " + request.getWidgetId() + ", comp: " + request.getComponentId());
            callback.call(new SwapItemResponse());
            return;
        }

        callback.call(new SwapItemResponse());
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleItemMenuActionRequest(ItemMenuActionRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new ItemMenuActionResponse());
            return;
        }

        if(!actionBindings.callItemAction(player, request.getWidgetId(), request.getComponentId(), request.getItemId(), request.getSlot(), request.getOption())) {
            logger.info("No binding for item menu action " + request.getWidgetId() + ", " + request.getComponentId() + ", " + request.getItemId());
            callback.call(new ItemMenuActionResponse());
            return;
        }

        callback.call(new ItemMenuActionResponse());
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleInterfaceItemMenuActionRequest(InterfaceItemMenuActionRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new InterfaceItemMenuActionResponse());
            return;
        }

        if(!actionBindings.callInterfaceItemAction(player, request.getWidgetId(), request.getComponentId(), request.getItemId(), request.getSlot(), request.getOption())) {
            logger.info("No binding for interface item menu action " + request.getWidgetId() + ", " + request.getComponentId() + ", " + request.getItemId());
            callback.call(new InterfaceItemMenuActionResponse());
            return;
        }

        callback.call(new InterfaceItemMenuActionResponse());
    }

    /**
     * Handles a request to publish a public submitPublicChat message..
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
    private void handleCommandRequest(CommandActionRequest request, Callback callback) {
        Player player = world.getPlayer(request.getPlayerId());
        if(player == null) {
            callback.call(new CommandResponse());
            return;
        }

        if(!actionBindings.callCommandAction(player, request.getName(), request.getArguments())) {
            logger.info("No binding for command, name: " + request.getName() + ", args: " + Arrays.toString(request.getArguments()));
            callback.call(new CommandResponse());
            return;
        }

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

    /**
     *
     * @param playerId
     * @param widgetId
     * @param componentId
     * @param itemId
     * @param slot
     * @param option
     * @param callback
     */
    public void handleInterfaceItemMenuAction(int playerId, int widgetId, int componentId, int itemId, int slot, int option, Callback<InterfaceItemMenuActionResponse> callback) {
        submit(new InterfaceItemMenuActionRequest(playerId, widgetId, componentId, itemId, slot, option), callback);
    }
}