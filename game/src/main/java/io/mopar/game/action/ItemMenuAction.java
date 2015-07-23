package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * Created by hadyn on 7/22/2015.
 */
public interface ItemMenuAction {
    void handle(Player player, int itemId, int slot);
}
