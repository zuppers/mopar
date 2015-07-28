package io.mopar.game.sync.npc;

import io.mopar.game.model.NPC;
import io.mopar.game.model.Player;
import io.mopar.game.model.Position;
import io.mopar.game.model.Step;
import io.mopar.game.sync.NpcDescriptor;
import io.mopar.game.sync.PlayerDescriptor;
import io.mopar.game.sync.block.AppearanceUpdateBlock;

/**
 * @author Hadyn Fitzgerald
 */
public class AddNpcDescriptor extends NpcDescriptor {



    private int playerId;

    /**
     * The position.
     */
    private Position position;

    /**
     *
     */
    private Position relative;

    /**
     * The last step.
     */
    private Step lastStep;
    private int type;

    /**
     * Constructs a new {@link AddNpcDescriptor};
     *
     * @param npc The npc.
     */
    public AddNpcDescriptor(NPC npc, Position relative) {
        super(npc);
        playerId = npc.getId();
        position = npc.getPosition();
        this.relative = relative;
        lastStep = npc.getLastStep();
        this.type = npc.getType();
        if(lastStep == null) {
            lastStep = Step.NORTH;
        }
    }

    public Position getPosition() {
        return position;
    }

    public Position getRelative() { return relative; }

    public int getNpcId() {
        return playerId;
    }

    public Step getLastStep() {
        return lastStep;
    }

    public int getType() {
        return type;
    }
}
