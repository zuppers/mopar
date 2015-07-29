package io.mopar.game.sync.npc;

import io.mopar.game.model.NPC;
import io.mopar.game.model.Player;
import io.mopar.game.model.Step;
import io.mopar.game.sync.NpcDescriptor;
import io.mopar.game.sync.PlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class WalkingNpcDescriptor extends NpcDescriptor {

    /**
     * The step.
     */
    private Step step;

    /**
     * Constructs a new {@link WalkingNpcDescriptor};
     *
     * @param player The player.
     */
    public WalkingNpcDescriptor(NPC player) {
        super(player);
        step = player.getLastStep();
    }

    public Step getStep() {
        return step;
    }
}