package io.mopar.game.event;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerDisplayUpdateEvent extends PlayerEvent {

    /**
     *
     * @param player
     */
    public PlayerDisplayUpdateEvent(Player player) {
        super(player);
    }
}
