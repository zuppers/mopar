package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class UpdateDisplayRequest extends Request {

    private int playerId;
    private int displayMode;

    public UpdateDisplayRequest(int playerId, int displayMode) {
        this.playerId = playerId;
        this.displayMode = displayMode;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getDisplayMode() {
        return displayMode;
    }
}
