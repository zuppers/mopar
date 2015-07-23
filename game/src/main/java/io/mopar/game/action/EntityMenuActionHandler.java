package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public interface EntityMenuActionHandler<T> {
    void handle(Player player, T entity, int option);
}
