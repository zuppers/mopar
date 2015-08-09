package io.mopar.game.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author Hadyn Fitzgerald
 */
public class ImmediateEventDispatcher implements EventDispatcher {

    /**
     *
     */
    private Multimap<Class<? extends Event>, EventHandler<?>> handlers = HashMultimap.create();

    /**
     *
     */
    public ImmediateEventDispatcher() {}

    /**
     *
     * @param eventClass
     * @param handler
     * @param <T>
     */
    public <T extends Event> void registerHandler(Class<T> eventClass, EventHandler<T> handler) {
        handlers.put(eventClass, handler);
    }

    /**
     *
     * @param event
     */
    @Override
    public void dispatch(Event event) {
        for(EventHandler handler : handlers.get(event.getClass())) {
            handler.handle(event);
        }
    }
}
