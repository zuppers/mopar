package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public interface CommandAction {

    /**
     *
     * @param player
     * @param command
     * @param arguments
     */
    void handle(Player player, String command, String[] arguments);
}
