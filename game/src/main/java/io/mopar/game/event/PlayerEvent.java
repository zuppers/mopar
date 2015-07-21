package io.mopar.game.event;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class PlayerEvent extends Event {

    /**
     *
     */
    private Player player;

    /**
     *
     * @param player
     */
    protected PlayerEvent(Player player) {
        this.player = player;
    }

    /**
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }
}
