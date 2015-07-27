package io.mopar.game.res;

import io.mopar.core.Response;
import io.mopar.game.model.Player;

/**
 * Created by hadyn on 6/20/15.
 */
public class NewPlayerResponse extends Response {

    public static final int OK = 0;
    public static final int FULL = 1;
    public static final int INVALID_PROFILE = 2;
    public static final int ALREADY_ONLINE = 3;

    /**
     * The status.
     */
    private int status;

    /**
     * The created player.
     */
    private Player player;

    /**
     * Constructs a new {@link NewPlayerResponse};
     *
     * @param status The response status.
     */
    public NewPlayerResponse(int status) {
        this.status = status;
    }

    /**
     * Gets the response status.
     *
     * @return The status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }
}
