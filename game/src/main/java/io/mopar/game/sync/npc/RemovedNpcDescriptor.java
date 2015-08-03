package io.mopar.game.sync.npc;

import io.mopar.game.model.NPC;
import io.mopar.game.model.Player;
import io.mopar.game.sync.NpcDescriptor;
import io.mopar.game.sync.PlayerDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class RemovedNpcDescriptor extends NpcDescriptor {

    /**
     * Constructs a new {@link RemovedNpcDescriptor};
     *
     * @param npc The npc.
     */
    public RemovedNpcDescriptor(NPC npc) {
        super(npc);
    }
}
