package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.config.SongConfig;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * @author Hadyn Fitzgerald
 */
public class SongModule implements LuaModule {

    /**
     * Constructs a new {@link SongModule};
     */
    public SongModule() {}

    /**
     * Parses song configurations from a JSON formatted string.
     *
     * @param json the json string.
     */
    public void ParseJson(String json) {
        SongConfig.parse(json);
    }

    /**
     * Gets a song configuration for its id.
     *
     * @param id the id.
     * @return the song configuration.
     */
    public LuaValue ForId(int id) {
        return Coerce.coerceToLua(SongConfig.forId(id));
    }

    /**
     * Gets a song configuration for its name.
     *
     * @param name the name.
     * @return the song configuration.
     */
    public LuaValue ForName(String name) {
        return Coerce.coerceToLua(SongConfig.forName(name));
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public LuaValue ForCoordinates(int x, int y) {
        return Coerce.coerceToLua(SongConfig.forCoordinates(x, y));
    }

    @Override
    public LuaTable getMetaTable() {
        LuaTable table = new LuaTable();
        table.set("__index", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue table, LuaValue key) {
                if(key.isint())     return ForId(key.checkint());
                if(key.isstring())  return ForName(key.checkjstring());
                throw new IllegalArgumentException("Expected either integer or string");
            }
        });
        return table;
    }

    @Override
    public String getNamespace() {
        return "song";
    }
}
