package io.mopar.game.sync.player;

import io.mopar.game.model.Player;
import io.mopar.game.model.Step;
import io.mopar.game.sync.PlayerDescriptor;

import java.util.List;
import java.util.Queue;

/**
 * @author Hadyn Fitzgerald
 */
public class RunningPlayerDescriptor extends PlayerDescriptor {

    /**
     * The steps.
     */
    private Queue<Step> steps;

    /**
     * Constructs a new {@link RunningPlayerDescriptor};
     *
     * @param player The player.
     */
    public RunningPlayerDescriptor(Player player) {
        super(player);
        steps = player.getRecentSteps();
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
