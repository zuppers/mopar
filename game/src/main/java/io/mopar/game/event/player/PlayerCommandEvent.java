package io.mopar.game.event.player;

import io.mopar.game.event.PlayerEvent;
import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerCommandEvent extends PlayerEvent {

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String[] arguments;

    /**
     *
     * @param player
     * @param name
     * @param arguments
     */
    public PlayerCommandEvent(Player player, String name, String[] arguments) {
        super(player);
        this.name = name;
        this.arguments = arguments;
    }

    /**
     *
     * @return
     */
    public String getCommand() {
        return name;
    }

    /**
     *
     * @return
     */
    public String[] getArguments() {
        return arguments;
    }
}
