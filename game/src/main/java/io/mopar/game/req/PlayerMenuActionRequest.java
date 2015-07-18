package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerMenuActionRequest extends Request {

    /**
     * The source player id.
     */
    private int playerId;

    /**
     * The target player id.
     */
    private int targetPlayerId;

    /**
     * The menu option.
     */
    private int option;

    /**
     * Constructs a new {@link PlayerMenuActionRequest};
     */
    public PlayerMenuActionRequest() {}

    /**
     * Gets the source player id.
     *
     * @return The player id.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Gets the target player id.
     *
     * @return The target player id.
     */
    public int getTargetPlayerId() {
        return targetPlayerId;
    }

    /**
     * Gets the menu option.
     *
     * @return The option.
     */
    public int getOption() {
        return option;
    }
}