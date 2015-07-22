package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public interface SwapItemAction {

    /**
     *
     * @param player
     * @param widgetId
     * @param componentId
     * @param firstSlot
     * @param secondSlot
     * @param mode
     */
    void handle(Player player, int widgetId, int componentId, int firstSlot, int secondSlot, int mode);
}
