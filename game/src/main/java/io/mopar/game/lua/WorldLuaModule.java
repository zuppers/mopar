package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.model.World;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaNumber;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldLuaModule implements LuaModule {

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
