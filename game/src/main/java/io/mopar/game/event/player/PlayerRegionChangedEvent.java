package io.mopar.game.event.player;

import io.mopar.game.event.PlayerEvent;
import io.mopar.game.model.Player;

/**
 * Created by hadyn on 7/30/2015.
 */
public class PlayerRegionChangedEvent extends PlayerEvent {

    /**
     *
     * @param player
     */
    public PlayerRegionChangedEvent(Player player) {
        super(player);
    }
}
