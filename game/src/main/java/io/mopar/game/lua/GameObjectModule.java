package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.config.GameObjectConfig;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * @author Hadyn Fitzgerald
 */
public class GameObjectModule implements LuaModule {

    /**
     * Constructs a new {@link GameObjectModule};
     */
    public GameObjectModule() {}

    /**
     * Parses a JSON formatted string to append configurations.
     *
     * @param json the json formatted string.
     */
    public void ParseJson(String json) {
        GameObjectConfig.parse(json);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id.
     * @return the configuration.
     */
    public GameObjectConfig ForId(int id) {
        return GameObjectConfig.forId(id);
    }

    /**
     * Gets a configuration for its name.
     *
     * @param name the name.
     * @return the configuration.
     */
    public GameObjectConfig ForName(String name) {
        return GameObjectConfig.forName(name);
    }

    @Override
    public LuaTable getMetaTable() {
        LuaTable table = new LuaTable();
        table.set("__index", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue table, LuaValue key) {
                if (key.isint()) return Coerce.coerceToLua(ForId(key.checkint()));
                if (key.isstring()) return Coerce.coerceToLua(ForName(key.checkjstring()));
                throw new IllegalArgumentException("Expected either integer or string");
            }
        });
        return table;
    }

    @Override
    public String getNamespace() {
        return "object";
    }
}
