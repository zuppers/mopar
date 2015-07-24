package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetVariableMessage extends Message {
    private int id;
    private int value;

    public SetVariableMessage(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}
