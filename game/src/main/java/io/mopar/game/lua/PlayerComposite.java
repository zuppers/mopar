package io.mopar.game.lua;

import io.mopar.core.lua.Composite;
import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerComposite extends EntityComposite {

    /**
     * The player.
     */
    private Player player;

    /**
     * Constructs a new {@link PlayerComposite};
     *
     * @param player The player.
     */
    public PlayerComposite(Player player) {
        super(player);
        this.player = player;
    }

    /**
     *
     * @param text
     */
    public void print(String text) {
        player.print(text);
    }
}
