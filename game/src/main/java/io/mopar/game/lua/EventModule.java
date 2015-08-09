package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.event.ImmediateEventDispatcher;
import io.mopar.game.event.player.PlayerCreatedEvent;
import io.mopar.game.event.player.PlayerDisplayUpdateEvent;
import io.mopar.game.event.player.PlayerRegionChangedEvent;
import org.luaj.vm2.LuaClosure;

/**
 * @author Hadyn Fitzgerald
 */
public class EventModule implements LuaModule {

    /**
     * The numeric enumerations for each event type
     * */
    public static final int PlayerCreated           = 0;
    public static final int PlayerRegionUpdated     = 1;
    public static final int PlayerDisplayUpdated    = 2;

    /**
     * The dispatcher.
     */
    private ImmediateEventDispatcher dispatcher;

    /**
     * Constructs a new {@link EventModule};
     *
     * @param dispatcher the dispatcher.
     */
    public EventModule(ImmediateEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Registers an event handler.
     *
     * @param type the event type.
     * @param closure the closure.
     */
    public void RegisterHandler(int type, LuaClosure closure) {
        switch (type) {
            case PlayerCreated:
                OnPlayerCreated(closure);
                break;
            case PlayerRegionUpdated:
                OnPlayerRegionUpdated(closure);
                break;
            case PlayerDisplayUpdated:
                OnPlayerDisplayUpdated(closure);
                break;
        }
    }

    /**
     * Binds an event handler that is triggered when a player is created.
     *
     * @param closure the closure.
     */
    public void OnPlayerCreated(LuaClosure closure) {
        dispatcher.registerHandler(PlayerCreatedEvent.class, (PlayerCreatedEvent evt) -> closure.call(Coerce.coerceToLua(evt.getPlayer())));
    }

    /**
     * Binds an event handler that is triggered when a players region is updated.
     *
     * @param closure the closure.
     */
    public void OnPlayerRegionUpdated(LuaClosure closure) {
        dispatcher.registerHandler(PlayerRegionChangedEvent.class, (evt) -> closure.invoke(Coerce.coerceToLua(evt.getPlayer())));
    }

    /**
     * Binds an event handler that is triggered when a players display is updated.
     *
     * @param closure the closure.
     */
    public void OnPlayerDisplayUpdated(LuaClosure closure) {
        dispatcher.registerHandler(PlayerDisplayUpdateEvent.class, (PlayerDisplayUpdateEvent event) -> closure.call(Coerce.coerceToLua(event.getPlayer())));
    }

    /**
     * Gets the namespace.
     *
     * @return the namespace for the module.
     */
    @Override
    public String getNamespace() {
        return "event";
    }
}
