package io.mopar.game.sync;

import io.mopar.game.model.Player;
import io.mopar.game.sync.block.AppearanceUpdateBlock;
import io.mopar.game.sync.block.ChatUpdateBlock;
import io.mopar.game.sync.player.IdlePlayerDescriptor;
import io.mopar.game.sync.player.RunningPlayerDescriptor;
import io.mopar.game.sync.player.WalkingPlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class PlayerDescriptor extends Descriptor<Player> {

    /**
     * Constructs a new {@link PlayerDescriptor};
     *
     * @param player The player.
     */
    protected PlayerDescriptor(Player player) {
        super(player);

        if(player.hasPublicChatMessage()) {
            addUpdateBlock(new ChatUpdateBlock(player.getPublicChatMessage()));
        }

        if(player.getAppearanceUpdated()) {
            addUpdateBlock(AppearanceUpdateBlock.create(player));
        }
    }

    /**
     * Creates a new player descriptor for the players current state.
     *
     * @param player The player.
     * @return The descriptor for the provided player.
     */
    public static PlayerDescriptor create(Player player) {
        if(!player.isMoving()) {
            return new IdlePlayerDescriptor(player);
        }
        return player.isRunning() ? new RunningPlayerDescriptor(player) : new WalkingPlayerDescriptor(player);
    }
}
