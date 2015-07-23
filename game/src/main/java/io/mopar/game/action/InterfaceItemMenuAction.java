package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * Created by hadyn on 7/23/2015.
 */
public interface InterfaceItemMenuAction {
    void handle(Player player, int slot, int itemId);
}
