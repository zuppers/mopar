package io.mopar.rs2.msg.login;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class ProfileMessage extends Message {
    private int id;

    public ProfileMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
