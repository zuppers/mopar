package io.mopar.game.event.player;

import io.mopar.game.event.PlayerEvent;
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
