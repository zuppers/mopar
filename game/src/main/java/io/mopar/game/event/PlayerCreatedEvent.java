package io.mopar.game.event;

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
