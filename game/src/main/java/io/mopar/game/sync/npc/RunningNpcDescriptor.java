package io.mopar.game.sync.npc;

import io.mopar.game.model.NPC;
import io.mopar.game.model.Direction;
import io.mopar.game.model.Step;
import io.mopar.game.sync.NpcDescriptor;

import java.util.Queue;

/**
 * @author Hadyn Fitzgerald
 */
public class RunningNpcDescriptor extends NpcDescriptor {

    /**
     * The steps.
     */
    private Queue<Step> steps;

    /**
     * Constructs a new {@link RunningNpcDescriptor};
     *
     * @param npc The npc.
     */
    public RunningNpcDescriptor(NPC npc) {
        super(npc);
        steps = npc.getRecentSteps();
    }

    /**
     * Gets the steps.
     *
     * @return The steps.
     */
    public Queue<Step> getSteps() {
        return steps;
    }
}
