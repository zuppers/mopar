package io.mopar.game.msg;

import io.mopar.core.msg.Message;
import io.mopar.game.model.Position;

/**
 * @author Hadyn Fitzgerald
 */
public class RebuildSceneMessage extends Message {

    /**
     * The position.
     */
    private Position position;

    /**
     * Constructs a new {@link RebuildSceneMessage};
     *
     * @param position The position.
     */
    public RebuildSceneMessage(Position position) {
        this.position = position;
    }

    /**
     * Gets the position.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }
}
