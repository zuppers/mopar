package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.config.InventoryConfig;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * @author Hadyn Fitzgerald
 */
public class InventoryModule implements LuaModule {

    /**
     * Parses a inventory configurations from a JSON formatted string.
     *
     * @param json the json string.
     */
    public void ParseJson(String json) {
        InventoryConfig.parse(json);
    }

    /**
     * Gets the size of an inventory.
     *
     * @param id the inventory id.
     * @return the size of the inventory.
     */
    public int Size(int id) {
        InventoryConfig config = ForId(id);
        if(config == null) return -1;
        return config.getSize();
    }

    /**
     * Gets the size of an inventory.
     *
     * @param name the inventory name.
     * @return the size of the inventory.
     */
    public int Size(String name) {
        InventoryConfig config = ForName(name);
        if(config == null) return -1;
        return config.getSize();
    }

    /**
     * Gets an inventory configuration for its id.
     *
     * @param id the inventory id.
     * @return the inventory configuration.
     */
    public InventoryConfig ForId(int id) {
        return InventoryConfig.forId(id);
    }

    /**
     * Gets an inventory configuration for its name.
     *
     * @param name the inventory name.
     * @return the inventory configuration.
     */
    public InventoryConfig ForName(String name) {
        return InventoryConfig.forName(name);
    }

    @Override
    public LuaTable getMetaTable() {
        LuaTable table = new LuaTable();
        table.set("__index", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue table, LuaValue key) {
                if(key.isint())     return Coerce.coerceToLua(ForId(key.checkint()));
                if(key.isstring())  return Coerce.coerceToLua(ForName(key.checkjstring()));
                throw new IllegalArgumentException("Expected either integer or string");
            }
        });
        return table;
    }

    @Override
    public String getNamespace() {
        return "inventory";
    }
}
