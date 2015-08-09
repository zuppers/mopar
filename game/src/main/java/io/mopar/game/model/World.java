package io.mopar.game.model;

import io.mopar.game.event.Event;
import io.mopar.game.event.EventDispatcher;
import io.mopar.game.event.player.PlayerRegionChangedEvent;
import io.mopar.game.model.block.Block;
import io.mopar.game.model.block.RegionSet;
import io.mopar.game.model.block.StillGraphicCreatedBlockEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class World {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(World.class);

    /**
     * The maximum amount of players allowed to be spawned in the world.
     */
    public static final int PLAYER_LIMIT = 2000;

    /**
     * The maximum amount of players allowed to be spawned in the world.
     */
    public static final int NPC_LIMIT = 16384;

    /**
     * The maximum amount of still graphics allowed to be spawned in the world.
     */
    public static final int STILL_GRAPHIC_LIMIT = 5000;

    /**
     * The event dispatcher.
     */
    private EventDispatcher dispatcher = (evt) -> logger.warn("Warning! Event dispatcher is unset, could not dispatch event: " + evt.getClass().getSimpleName());

    /**
     * The players.
     */
    private EntityList<Player> players = new EntityList<>(PLAYER_LIMIT);

    /**
     * The mapping of players by their user id.
     */
    private Map<Long, Player> playersByUid = new HashMap<>();

    /**
     * The npcs.
     */
    private EntityList<NPC> npcs = new EntityList<>(NPC_LIMIT);

    /**
     * The still graphics.
     */
    private EntityList<StillGraphic> stillGraphics = new EntityList<>(STILL_GRAPHIC_LIMIT);

    /**
     * The state handler bindings.
     */
    private StateBindings stateHandlers = new StateBindings();

    /**
     * The regions.
     */
    private RegionSet regions = new RegionSet();

    /**
     * The current time.
     */
    private int time;

    /**
     * Constructs a new {@link World};
     */
    public World() {}

    /**
     * Sets the event dispatcher.
     *
     * @param dispatcher the event dispatcher.
     */
    public void setEventDispatcher(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Adds a player to the world.
     *
     * @param player The player to add.
     * @return If the player was successfully added.
     */
    public boolean addPlayer(Player player) {

        // Check if there are any players registered under the given player's user id
        // we cannot have two players with the same name, so this will cause to fail
        if(playersByUid.containsKey(player.getUid())) {
            return false;
        }

        playersByUid.put(player.getUid(), player);
        return players.add(player);
    }

    /**
     * Removes a player.
     *
     * @param playerId The player id.
     * @return if the player was successfully removed, <code>false</code> if the specified id does not reference
     *          a player that exists.
     */
    public boolean removePlayer(int playerId) {
        Player player = players.remove(playerId);
        if(player == null) {
            return false;
        }
        playersByUid.remove(player.getUid());
        return true;
    }

    /**
     * Gets if the world contains a player.
     *
     * @param uid the player's user id.
     * @return if the world contains the player.
     */
    public boolean hasPlayer(long uid) {
        return playersByUid.containsKey(uid);
    }

    /**
     * Gets a player by its id.
     *
     * @param id The id of the player.
     * @return The player for the specified id or <code>null</code> if the player does not exist.
     */
    public Player getPlayer(int id) {
        return players.get(id);
    }

    /**
     * Gets the players.
     *
     * @return The players.
     */
    public EntityList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets if a player exists.
     *
     * @param playerId the player id.
     * @return if the player exists.
     */
    public boolean playerExists(int playerId) {
        return players.get(playerId) != null;
    }

    /**
     * Gets the amount of players in the world.
     *
     * @return the amount of players.
     */
    public int getAmountPlayers() {
        return players.size();
    }

    /**
     * Creates a game object.
     *
     * @param plane the plane.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param type the object type.
     * @param configId the configuration id.
     * @param orientation the object orientation.
     *
     * @see GameObjectGroup
     * @see ObjectOrientation
     */
    public void createGameObject(int plane, int x, int y, int type, int configId, int orientation) {
        Block block = regions.getBlock(plane, x >> 3, y >> 3);
        block.updateGameObject(x & 7, y & 7, type, configId, orientation);
    }

    /**
     * Removes a game object.
     *
     * @param plane the plane.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param group the object group.
     *
     * @see GameObjectGroup
     */
    public void removeGameObject(int plane, int x, int y, int group) {
        Block block = regions.getBlock(plane, x >> 3, y >> 3);
        block.removeGameObject(x & 7, y & 7, group);
    }

    /**
     * Creates a new still graphic.
     *
     * @param plane the plane.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param type the type.
     * @param height the height offset.
     * @param delay the delay.
     * @return if the still graphic was successfully created.
     */
    public boolean createStillGraphic(int plane, int x, int y, int type, int height, int delay) {
        StillGraphic stillGraphic = new StillGraphic(new Graphic(type, height, delay));
        stillGraphic.setPosition(new Position(x, y, plane));
        return addStillGraphic(stillGraphic);
    }

    /**
     * Adds a still graphic.
     *
     * @param stillGraphic the still graphic.
     * @return if the still graphic was successfully added.
     */
    public boolean addStillGraphic(StillGraphic stillGraphic) {
        if(!stillGraphics.add(stillGraphic)) {
            return false;
        }

        Position position = stillGraphic.getPosition();

        Block block = regions.getBlock(position);
        block.addEvent(new StillGraphicCreatedBlockEvent(stillGraphic));
        return true;
    }

    /**
     * Helper method; registers a player state handler.
     *
     * @param stateId The state id.
     * @param handler The state handler.
     */
    public void registerPlayerStateHandler(int stateId, StateHandler<Player> handler) {
        stateHandlers.registerPlayerStateHandler(stateId, handler);
    }

    /**
     * Gets the regions.
     *
     * @return the regions.
     */
    public RegionSet getRegions() {
        return regions;
    }

    /**
     * Gets the time.
     *
     * @return The time.
     */
    public int getTime() {
        return time;
    }

    /**
     * Updates the world.
     */
    public void update() {
        time++;

        // Update all of the players
        for(Player player : players) {
            handlePlayerState(player);
            updateMovement(player);

            if(player.isMoving()) {
                Position previous = player.getPreviousPosition();
                Position current  = player.getPosition();

                // Compare the previous positions region with the current, if the player has
                // moved regions send an event. If the previous position is null then we can
                // assume that the region was changed since we just entered
                if(previous.getRegionHash() != current.getRegionHash()) {
                    dispatch(new PlayerRegionChangedEvent(player));
                }
            }
        }

        // Update all of the npcs
        for(NPC npc : npcs) {
            handleNpcState(npc);
            updateMovement(npc);
        }

        // Update the player scene graphs
        for(Player player : players) {
            player.updateLocalPlayers(players);
            player.updateLocalNpcs(npcs);
            player.updateBlocks(regions);

            // Its anal about this for some reason
            if(player.getRebuildScene()) {
                player.rebuildScene();
            }
        }

        // Reset all of the players
        for(Player player : players) {
            player.reset();
        }

        stillGraphics.clear();
        regions.reset();
    }

    /**
     * Dispatches an event.
     *
     * @param event the event to dispatch.
     */
    private void dispatch(Event event) {
        dispatcher.dispatch(event);
    }

    /**
     * Handles the next queued player state, or if there are no queued states the player is said to be {@link States#IDLE}.
     *
     * @param player The player to handle.
     */
    private void handlePlayerState(Player player) {
        int state = player.hasPendingState() ? player.nextState() : States.IDLE;
        if(!stateHandlers.handlePlayerState(player, state)) {
            //logger.warn("No player state handler registered for state " + state);
        }
    }

    /**
     * Handles the next queued npc state, or if there are no queued states the player is said to be {@link States#IDLE}.
     *
     * @param npc The npc to handle.
     */
    private void handleNpcState(NPC npc) {
        int state = npc.hasPendingState() ? npc.nextState() : States.IDLE;
    }

    /**
     * Updates the movement for a mobile.
     *
     * @param mobile The mobile.
     */
    private void updateMovement(Mobile mobile) {
        mobile.setPreviousPosition(mobile.getPosition());

        if(mobile.hasPendingSteps() || mobile.hasPendingWaypoints()) {
            Position source = mobile.getPosition();
            Position current = source;

            int steps = mobile.isRunning() ? 2 : 1;
            while(steps-- > 0) {

                // If the mobile has no pending steps and has pending waypoints, interpolate between the current position
                // and the next waypoint and append those calculated steps to the mobiles step queue
                if(!mobile.hasPendingSteps() && mobile.hasPendingWaypoints()) {
                    Waypoint waypoint = mobile.nextWaypoint();
                    mobile.addSteps(waypoint.interpolate(mobile.getPosition()));
                }

                // If there are no pending steps after processing the next waypoint then we can assume the route is finished
                if(!mobile.hasPendingSteps()) {
                    break;
                }


                Step step = mobile.nextStep();

                // If the mobile is clipped then check if the next step is traversable
                if(mobile.isClipped() && !regions.isTraversable(current, step.getDirection(), TraversalMap.TRAVERSE_WALKING)) {
                    mobile.clearRoute();
                    break;
                }

                mobile.setPosition(current = current.offset(step.asVector()));
                mobile.addPreviousStep(step);
                step.setTime(time);

                mobile.setMoving(true);
            }
        }
    }
}