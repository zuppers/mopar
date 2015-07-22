package io.mopar.game.cmd;

import io.mopar.game.event.EventHandler;
import io.mopar.game.event.player.PlayerCommandEvent;
import io.mopar.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Hadyn Fitzgerald
 */
public interface CommandHandler {

     Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    /**
     *
     * @param player
     * @param name
     * @param args
     */
    void handle(Player player, String name, String[] args);

    /**
     *
     * @return
     */
    default EventHandler<PlayerCommandEvent> wrap(String name, int rights) {
        return (event) ->  {
            try {
                if (name.equals(event.getCommand()) && event.getPlayer().getRights() >= rights) {
                    handle(event.getPlayer(), event.getCommand(), event.getArguments());
                }
            } catch (Throwable t) {
                logger.error("Issue with handling command", t);
            }
        };
    }
}
