package io.mopar.game.event.player;

import io.mopar.game.event.PlayerEvent;
import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerCreatedEvent extends PlayerEvent {

    /**
     * Constructs a new {@link PlayerCreatedEvent};
     */
    public PlayerCreatedEvent(Player player) {
        super(player);
    }
}
