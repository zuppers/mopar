package io.mopar.game.sync.npc;

import io.mopar.game.model.NPC;
import io.mopar.game.model.Direction;
import io.mopar.game.model.Step;
import io.mopar.game.sync.NpcDescriptor;

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
     * @param npc The npc.
     */
    public WalkingNpcDescriptor(NPC npc) {
        super(npc);
        step = npc.getLastStep();
    }

    /**
     *
     * @return
     */
    public Step getStep() {
        return step;
    }
}
