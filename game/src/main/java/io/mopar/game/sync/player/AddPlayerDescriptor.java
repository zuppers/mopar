package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.model.Position;
import io.mopar.game.model.Direction;
import io.mopar.game.model.Step;
import io.mopar.game.sync.PlayerDescriptor;
import io.mopar.game.sync.block.AppearanceUpdateBlock;

/**
 * @author Hadyn Fitzgerald
 */
public class AddPlayerDescriptor extends PlayerDescriptor {

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
     * Constructs a new {@link AddPlayerDescriptor};
     *
     * @param player The player.
     */
    public AddPlayerDescriptor(Player player, Position relative) {
        super(player);
        id = player.getId();
        position = player.getPosition();
        this.relative = relative;
        lastStep = player.getLastStep();
        if(lastStep == null) {
            lastStep = new Step(Direction.NORTH);
        }

        if(!hasUpdateBlock(AppearanceUpdateBlock.class)) {
            addUpdateBlock(AppearanceUpdateBlock.create(player));
        }
    }

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
    public Step getLastStep() {
        return lastStep;
    }
}
