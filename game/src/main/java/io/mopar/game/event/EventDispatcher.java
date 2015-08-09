package io.mopar.game.event;

/**
 * @author Hadyn Fitzgerald
 */
public interface EventDispatcher {

    /**
     * Dispatches an event.
     *
     * @param event the event to dispatch.
     */
    void dispatch(Event event);
}