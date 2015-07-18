package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.sync.PlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class IdlePlayerDescriptor extends PlayerDescriptor {

    /**
     * Constructs a new {@link IdlePlayerDescriptor};
     *
     * @param player The player.
     */
    public IdlePlayerDescriptor(Player player) {
        super(player);
    }
}
