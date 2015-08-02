package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.GameService;
import io.mopar.game.event.*;
import io.mopar.game.event.player.PlayerCreatedEvent;
import io.mopar.game.event.player.PlayerDisplayUpdateEvent;
import org.luaj.vm2.LuaClosure;

/**
 * @author Hadyn Fitzgerald
 */
public class ServiceModule implements LuaModule {

    /**
     *
     */
    public static final int player_created = 0;

    /**
     *
     */
    public static final int player_display_updated = 1;

    /**
     *
     */
    private GameService service;

    /**
     *
     * @param service
     */
    public ServiceModule(GameService service) {
        this.service = service;
    }

    /**
     *
     * @param type
     * @param closure
     */
    public void on_event(int type, LuaClosure closure) {
        switch (type) {
            case player_created:
                on_player_created(closure);
                break;
            case player_display_updated:
                on_player_display_updated(closure);
                break;
            default:
                throw new IllegalArgumentException("Unhandled event type");
        }
    }

    /**
     *
     * @param closure
     */
    public void on_player_created(LuaClosure closure) {
        service.registerEventHandler(PlayerCreatedEvent.class, (PlayerCreatedEvent evt) -> closure.call(Coerce.coerceToLua(evt.getPlayer())));
    }

    /**
     *
     * @param closure
     */
    public void on_player_display_updated(LuaClosure closure) {
        service.registerEventHandler(PlayerDisplayUpdateEvent.class, (PlayerDisplayUpdateEvent event) -> closure.call(Coerce.coerceToLua(event.getPlayer())));
    }

    /**
     *
     * @return
     */
    @Override
    public String getNamespace() {
        return "service";
    }
}
