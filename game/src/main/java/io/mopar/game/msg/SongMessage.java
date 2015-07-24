package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * Created by hadyn on 7/23/2015.
 */
public class SongMessage extends Message {
    private int id;

    public SongMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
