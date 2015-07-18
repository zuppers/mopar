package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class RemovePlayerRequest extends Request {

    /**
     * The player id.
     */
    private int playerId;

    /**
     * Constructs a new {@link RemovePlayerRequest};
     *
     * @param playerId The player id.
     */
    public RemovePlayerRequest(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player id.
     *
     * @return the player id.
     */
    public int getPlayerId() {
        return playerId;
    }
}
