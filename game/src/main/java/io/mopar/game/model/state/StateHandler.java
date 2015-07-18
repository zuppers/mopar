package io.mopar.game.model.state;

import io.mopar.game.model.Mobile;

/**
 * @author Hadyn Fitzgerald
 */
public interface StateHandler<T extends Mobile> {

    /**
     * Handles a state.
     *
     * @param mobile The mobile.
     * @param state The state.
     */
    void handle(T mobile, int state);
}
