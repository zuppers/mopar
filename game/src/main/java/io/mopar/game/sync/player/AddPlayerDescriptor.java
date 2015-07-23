package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.model.Position;
import io.mopar.game.model.Step;
import io.mopar.game.sync.PlayerDescriptor;
import io.mopar.game.sync.block.AppearanceUpdateBlock;

/**
 * @author Hadyn Fitzgerald
 */
public class AddPlayerDescriptor extends PlayerDescriptor {



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

    /**
     * Constructs a new {@link io.mopar.game.sync.PlayerDescriptor};
     *
     * @param player The player.
     */
    public AddPlayerDescriptor(Player player, Position relative) {
        super(player);
        playerId = player.getId();
        position = player.getPosition();
        this.relative = relative;
        lastStep = player.getLastStep();
        if(lastStep == null) {
            lastStep = Step.NORTH;
        }

        if(!hasUpdateBlock(AppearanceUpdateBlock.class)) {
            addUpdateBlock(AppearanceUpdateBlock.create(player));
        }
    }

    public Position getPosition() {
        return position;
    }

    public Position getRelative() { return relative; }

    public int getPlayerId() {
        return playerId;
    }

    public Step getLastStep() {
        return lastStep;
    }
}
