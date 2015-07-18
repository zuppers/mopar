package io.mopar.game.action;

import io.mopar.game.model.Player;

/**
 * @author Hadyn Fitzgerald
 */
public interface EntityMenuAction<T> {
    void handle(Player player, T entity, int option);
}
