package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * Created by hadyn on 8/6/2015.
 */
public class ExecuteScriptMessage extends Message {
    private int id;

    public ExecuteScriptMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
