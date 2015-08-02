package io.mopar.game.sync.npc;

import io.mopar.game.model.NPC;
import io.mopar.game.model.Position;
import io.mopar.game.model.Direction;
import io.mopar.game.model.Step;
import io.mopar.game.sync.NpcDescriptor;

/**
 * @author Hadyn Fitzgerald
 */
public class AddNpcDescriptor extends NpcDescriptor {

    /**
     *
     */
    private int id;

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

    /**
     *
     */
    private int type;

    /**
     * Constructs a new {@link AddNpcDescriptor};
     *
     * @param npc The npc.
     */
    public AddNpcDescriptor(NPC npc, Position relative) {
        super(npc);
        id = npc.getId();
        position = npc.getPosition();
        this.relative = relative;
        lastStep = npc.getLastStep();
        this.type = npc.getType();
        if(lastStep == null) {
            lastStep = new Step(Direction.NORTH);
        }
    }

    /**
     *
     * @return
     */
    public Position getPosition() {
        return position;
    }

    /**
     *
     * @return
     */
    public Position getRelative() { return relative; }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public Step getLastStep() {
        return lastStep;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }
}
