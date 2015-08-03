package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class PlaySongMessage extends Message {

    /**
     * The song id.
     */
    private int id;

    /**
     * Constructs a new {@link PlaySongMessage};
     *
     * @param id the song id.
     */
    public PlaySongMessage(int id) {
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }
}
