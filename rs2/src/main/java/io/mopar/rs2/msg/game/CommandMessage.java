package io.mopar.rs2.msg.game;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class CommandMessage extends Message {

    private String name;
    private String[] args;

    public CommandMessage(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public String[] getArguments() {
        return args;
    }
}