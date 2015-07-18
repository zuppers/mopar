package io.mopar.game.model;

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
    private MobileSceneGraph<Player> localPlayers = new MobileSceneGraph<>(World.PLAYER_CAPACITY);

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
}
