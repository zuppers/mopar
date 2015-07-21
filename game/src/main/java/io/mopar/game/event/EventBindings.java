package io.mopar.game.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @autor Hadyn Fitzgerald
 */
public class EventBindings {

    /**
     *
     */
    private Multimap<Class<? extends Event>, EventHandler<?>> handlers = HashMultimap.create();

    /**
     *
     */
    public EventBindings() {}

    /**
     *
     * @param eventClass
     * @param handler
     * @param <T>
     */
    public <T extends Event> void add(Class<? extends Event> eventClass, EventHandler<T> handler) {
        handlers.put(eventClass, handler);
    }

    /**
     *
     * @param event
     */
    public void handle(Event event) {
        for(EventHandler handler : handlers.get(event.getClass())) {
            handler.handle(event);
        }
    }
}
