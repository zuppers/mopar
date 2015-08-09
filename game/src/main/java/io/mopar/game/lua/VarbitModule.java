package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.config.VarbitConfig;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * @author Hadyn Fitzgerald
 */
public class VarbitModule implements LuaModule {
    
    /**
     * Constructs a new {@link VarbitModule};
     */
    public VarbitModule() {}

    /**
     * Parses a JSON formatted string to append configurations.
     *
     * @param json the json formatted string.
     */
    public void ParseJson(String json) {
        VarbitConfig.parse(json);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id.
     * @return the configuration.
     */
    public VarbitConfig ForId(int id) {
        return VarbitConfig.forId(id);
    }

    /**
     * Gets a configuration for its name.
     *
     * @param name the name.
     * @return the configuration.
     */
    public VarbitConfig ForName(String name) {
        return VarbitConfig.forName(name);
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
        return "varbit";
    }
}
