package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.sync.PlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class RemovedPlayerDescriptor extends PlayerDescriptor {
    /**
     * Constructs a new {@link io.mopar.game.sync.PlayerDescriptor};
     *
     * @param player The player.
     */
    public RemovedPlayerDescriptor(Player player) {
        super(player);
    }
}
