package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.model.Step;
import io.mopar.game.sync.PlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class WalkingPlayerDescriptor extends PlayerDescriptor {

    /**
     * The step.
     */
    private Step step;

    /**
     * Constructs a new {@link WalkingPlayerDescriptor};
     *
     * @param player The player.
     */
    public WalkingPlayerDescriptor(Player player) {
        super(player);
        step = player.getLastStep();
    }

    public Step getStep() {
        return step;
    }
}
