package io.mopar.game.model;

import io.mopar.game.model.Mobile;

/**
 * @author Hadyn Fitzgerald
 */
public interface StateHandler<T extends Mobile> {

    /**
     * Handles a state.
     *
     * @param mobile The mobile.
     */
    void handle(T mobile);
}
