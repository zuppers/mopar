package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class CommandActionRequest extends Request {
    private int playerId;
    private String name;
    private String[] arguments;

    public CommandActionRequest(int playerId, String name, String[] arguments) {
        this.playerId = playerId;
        this.name = name;
        this.arguments = arguments;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public String[] getArguments() {
        return arguments;
    }
}
