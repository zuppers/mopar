package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.event.player.PlayerRegionUpdatedEvent;
import io.mopar.game.model.World;
import org.luaj.vm2.LuaClosure;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldLuaModule implements LuaModule {

    public static final int player_region_updated = 0;

    /**
     * The world.
     */
    private World world;

    /**
     * Constructs a new {@link WorldLuaModule};
     *
     * @param world The world.
     */
    public WorldLuaModule(World world) {
        this.world = world;
    }

    /**
     *
     * @param type
     * @param closure
     */
    public void on(int type, LuaClosure closure) {
        switch (type) {
            case player_region_updated:
                on_player_region_updated(closure);
                break;
            default:
                throw new IllegalArgumentException("Unhandled event type");
        }
    }

    /**
     *
     * @param closure
     */
    public void on_player_region_updated(LuaClosure closure) {
        world.registerEventHandler(PlayerRegionUpdatedEvent.class, (evt) -> closure.invoke(Coerce.coerceToLua(evt.getPlayer())));
    }

    /**
     * Registers a player state handler.
     *
     * @param stateId The state id.
     * @param closure The lua closure.
     */
    public void on_player_state(int stateId, LuaClosure closure) {
        world.registerPlayerStateHandler(stateId, (plr) -> closure.call(Coerce.coerceToLua(plr)));
    }

    /**
     * Registers a NPC state handler.
     *
     * @param stateId The state id.
     * @param closure The lua closure.
     */
    public void on_npc_state(int id, int stateId, LuaClosure closure) {

    }

    /**
     *
     * @param plane
     * @param x
     * @param y
     * @param type
     * @param config
     * @param orientation
     */
    public void create_obj(int plane, int x, int y, int type, int config, int orientation) {
        world.createGameObject(plane, x, y, type, config, orientation);
    }

    /**
     *
     * @param plane
     * @param x
     * @param y
     * @param group
     */
    public void remove_obj(int plane, int x, int y, int group) {
        world.removeGameObject(plane, x, y, group);
    }

    /**
     *
     * @param plane
     * @param x
     * @param y
     * @param config
     * @param height
     * @param delay
     */
    public void create_still_gfx(int plane, int x, int y, int config, int height, int delay) {
        world.createStillGraphic(plane, x, y, config, height, delay);
    }

    /**
     * Gets the current world time.
     *
     * @return The time.
     */
    public int time() {
        return world.getTime();
    }


    /**
     * Gets the namespace.
     *
     * @return The namespace.
     */
    @Override
    public String getNamespace() {
        return "world";
    }
}
