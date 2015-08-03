package io.mopar.game.model;

import io.mopar.game.model.block.RegionSet;

/**
 * @author Hadyn Fitzgerald
 */
public class Scene {

    /**
     * The length of a scene.
     */
    public static final int LENGTH = 104;

    /**
     * The distance that is visible.
     */
    public static final int VISIBLE_DISTANCE = 16;

    /**
     * The local players.
     */
    private MobileSceneGraph<Player> localPlayers = new MobileSceneGraph<>(World.PLAYER_LIMIT);

    /**
     * The local npcs.
     */
    private MobileSceneGraph<NPC> localNpcs = new MobileSceneGraph<>(World.NPC_LIMIT);

    /**
     *
     */
    private BlockSceneGraph immobiles = new BlockSceneGraph();

    /**
     * The position.
     */
    private Position position;

    /**
     * Checks if the scene has been built at least once.
     */
    private void checkBuilt() {
        if(position == null) {
            throw new IllegalStateException("Scene must be built before modifying");
        }
    }

    /**
     * Updates the local players.
     *
     * @param players The players.
     * @param currentPosition The current position.
     */
    public void updateLocalPlayers(EntityList<Player> players, Position currentPosition) {
        checkBuilt();
        localPlayers.update(players, position, currentPosition, LENGTH >> 1, VISIBLE_DISTANCE);
    }

    /**
     *
     * @param npcs
     * @param currentPosition
     */
    public void updateLocalNpcs(EntityList<NPC> npcs, Position currentPosition) {
        checkBuilt();
        localNpcs.update(npcs, position, currentPosition, LENGTH >> 1, VISIBLE_DISTANCE);
    }

    /**
     *
     * @param regions
     * @param currentPosition
     */
    public void updateBlocks(RegionSet regions, Position currentPosition) {
        checkBuilt();
        immobiles.update(regions, position, currentPosition, VISIBLE_DISTANCE, LENGTH >> 1);
    }

    /**
     * Builds the scene at the given position.
     *
     * @param position The position.
     */
    public void build(Position position) {
        this.position = position;
    }

    /**
     * Checks if the scene needs to be built, this occurs if the viewport exits the scan area.
     *
     * @param compare The position.
     * @return if the scene needs to be built.
     */
    public boolean checkRebuild(Position compare) {
        checkBuilt();
        return !position.within(compare, (LENGTH >> 1) - VISIBLE_DISTANCE);
    }

    /**
     * Gets the center position of the scene.
     *
     * @return the position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the local players.
     *
     * @return the local players.
     */
    public MobileSceneGraph<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * Gets the local npcs.
     *
     * @return the local npcs.
     */
    public MobileSceneGraph<NPC> getLocalNpcs() { return localNpcs; }

    public BlockSceneGraph getBlockGraph() {
        return immobiles;
    }
}
