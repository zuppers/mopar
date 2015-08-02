package io.mopar.game.event.player;

import io.mopar.game.event.PlayerEvent;
import io.mopar.game.model.Player;

/**
 * Created by hadyn on 7/30/2015.
 */
public class PlayerRegionUpdatedEvent extends PlayerEvent {

    /**
     *
     * @param player
     */
    public PlayerRegionUpdatedEvent(Player player) {
        super(player);
    }
}
