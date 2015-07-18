package io.mopar.rs2.game;

import io.mopar.game.model.Player;
import io.mopar.rs2.net.Attachment;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerSessionContext implements Attachment {

    private Player player;

    public PlayerSessionContext(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerId() {
        return player.getId();
    }
}
