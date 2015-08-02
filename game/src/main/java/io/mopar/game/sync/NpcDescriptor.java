package io.mopar.game.sync;

import io.mopar.game.model.NPC;
import io.mopar.game.sync.npc.IdleNpcDescriptor;
import io.mopar.game.sync.npc.RunningNpcDescriptor;
import io.mopar.game.sync.npc.WalkingNpcDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class NpcDescriptor extends Descriptor<NPC> {

    /**
     * Constructs a new {@link NpcDescriptor};
     *
     * @param mobile The mobile.
     */
    protected NpcDescriptor(NPC mobile) {
        super(mobile);
    }

    /**
     * Creates a new npc descriptor for the npcs current state.
     *
     * @param npc The npc.
     * @return The descriptor for the provided player.
     */
    public static NpcDescriptor create(NPC npc) {
        if(!npc.isMoving()) {
            return new IdleNpcDescriptor(npc);
        }
        return npc.isRunning() ? new RunningNpcDescriptor(npc) : new WalkingNpcDescriptor(npc);
    }
}
