package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.model.Position;
import io.mopar.game.sync.PlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class TeleportingPlayerDescriptor extends PlayerDescriptor {

    private Position relative;
    private Position position;
    private boolean clearWaypoints;

    /**
     * Constructs a new {@link PlayerDescriptor};
     *
     * @param player The player.
     */
    public TeleportingPlayerDescriptor(Player player, Position relative) {
        super(player);
        this.position = player.getPosition();
        this.relative = relative;
    }

    /**
     * Gets the position.
     *
     * @return the position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the relative position.
     *
     * @return the relative position.
     */
    public Position getRelativePosition() { return relative; }

    /**
     * Gets the clear waypoints flag.
     *
     * @return the clear waypoints flag.
     */
    public boolean getClearWaypoints() {
        return clearWaypoints;
    }
}
