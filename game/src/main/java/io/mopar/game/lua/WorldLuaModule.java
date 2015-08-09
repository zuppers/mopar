package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.model.GameObjectGroup;
import io.mopar.game.model.ObjectOrientation;
import io.mopar.game.model.World;
import org.luaj.vm2.LuaClosure;

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
     * @param world the world.
     */
    public WorldLuaModule(World world) {
        this.world = world;
    }

    /**
     * Creates a new game object.
     *
     * @param plane the plane.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param type the object type.
     * @param config the configuration id.
     * @param orientation the orientation.
     *
     * @see GameObjectGroup
     * @see ObjectOrientation
     */
    public void CreateObject(int plane, int x, int y, int type, int config, int orientation) {
        world.createGameObject(plane, x, y, type, config, orientation);
    }

    /**
     * Removes a game object.
     *
     * @param plane the plane.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param group the object group.
     *
     * @see GameObjectGroup
     */
    public void RemoveObject(int plane, int x, int y, int group) {
        world.removeGameObject(plane, x, y, group);
    }

    /**
     * Creates a new still graphic.
     *
     * @param plane the plane.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param configId the config id.
     * @param height the height from the ground.
     * @param delay the delay.
     */
    public void CreateStillGraphic(int plane, int x, int y, int configId, int height, int delay) {
        world.createStillGraphic(plane, x, y, configId, height, delay);
    }

    /**
     * Registers a player state handler.
     *
     * @param id the state id.
     * @param closure the closure.
     */
    public void OnPlayerState(int id, LuaClosure closure) {
        world.registerPlayerStateHandler(id, (plr) -> closure.call(Coerce.coerceToLua(plr)));
    }

    /**
     * Gets the current world time.
     *
     * @return The time.
     */
    public int Time() {
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
