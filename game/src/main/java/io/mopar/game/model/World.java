package io.mopar.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static final int PLAYER_CAPACITY = 2000;

    /**
     * The maximum amount of players allowed to be spawned in the world.
     */
    public static final int NPC_CAPACITY = 16384;

    /**
     * The players.
     */
    private EntityList<Player> players = new EntityList<>(PLAYER_CAPACITY);

    /**
     * The NPCs.
     */
    private EntityList<NPC> npcs = new EntityList<>(NPC_CAPACITY);

    /**
     * The state handler bindings.
     */
    private StateBindings stateHandlers = new StateBindings();

    /**
     * The current time.
     */
    private int time;

    /**
     * Adds a player to the world.
     *
     * @param player The player to add.
     * @return If the player was successfully added.
     */
    public boolean addPlayer(Player player) {
        return players.add(player);
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
     * Removes a player.
     *
     * @param playerId The player id.
     * @return if the player was successfully removed, <code>false</code> if the specified id does not reference
     *          a player that exists.
     */
    public boolean removePlayer(int playerId) {
        return players.remove(playerId) != null;
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
     * Helper method; registers a player state handler.
     *
     * @param stateId The state id.
     * @param handler The state handler.
     */
    public void registerPlayerStateHandler(int stateId, StateHandler<Player> handler) {
        stateHandlers.registerPlayerStateHandler(stateId, handler);
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
        }

        // Update all of the npcs
        for(NPC npc : npcs) {
            handleNpcState(npc);
            updateMovement(npc);
        }

        // Update the player scene graphs
        for(Player player : players) {
            player.updateLocalPlayers(players);

            // Its anal about this for some reason
            if(player.getRebuildScene()) {
                player.rebuildScene();
            }
        }

        // Reset all of the players
        for(Player player : players) {
            player.reset();
        }
    }

    /**
     * Handles the next queued player state, or if there are no queued states the player is said to be {@link States#IDLE}.
     *
     * @param player The player to handle.
     */
    private void handlePlayerState(Player player) {
        int state = player.hasPendingState() ? player.nextState() : States.IDLE;
        if(!stateHandlers.handlePlayerState(player, state)) {
            logger.warn("No player state handler registered for state " + state);
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
        if(mobile.hasSteps()) {
            Position source = mobile.getPosition();
            Position current = source;

            int steps = mobile.isRunning() ? 2 : 1;
            while(steps-- > 0 && mobile.hasSteps()) {
                Step step = mobile.nextStep();
                mobile.setPosition(current = current.offset(step.asVector()));
                mobile.addStepRecord(new StepRecord(step, time));
            }

            mobile.setMoving(true);
        }
    }

    /**
     * Gets the time.
     *
     * @return The time.
     */
    public int getTime() {
        return time;
    }
}
