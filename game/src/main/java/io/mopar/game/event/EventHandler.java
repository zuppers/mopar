package io.mopar.game.event;

/**
 * @author Hadyn Fitzgerald
 */
public interface EventHandler<T extends Event> {

    /**
     * Handles the event.
     *
     * @param event the event to handle.
     */
    void handle(T event);
}
