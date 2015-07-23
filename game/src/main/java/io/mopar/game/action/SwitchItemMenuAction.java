package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public interface SwitchItemMenuAction {

    /**
     *
     * @param player
     * @param firstSlot
     * @param secondSlot
     * @param mode
     */
    void handle(Player player, int firstSlot, int secondSlot, int mode);
}
