package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.config.InterfaceComponent;
import io.mopar.game.config.InterfaceConfig;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * @author Hadyn Fitzgerald
 */
public class InterfaceModule implements LuaModule {

    /**
     *
     */
    public static final int Closable = 0;

    /**
     *
     */
    public static final int Static = 1;

    /**
     * Parses interface configurations from a JSON formatted string.
     *
     * @param json the json string.
     */
    public void ParseJson(String json) {
        InterfaceConfig.parse(json);
    }

    /**
     * Gets a configuration for its name.
     *
     * @param id the id of the configuration.
     * @return the configuration.
     */
    public InterfaceConfig ForId(int id) {
        return InterfaceConfig.forId(id);
    }

    /**
     * Gets a configuration for its name.
     *
     * @param name the name of the configuration.
     * @return the configuration.
     */
    public InterfaceConfig ForName(String name) {
        return InterfaceConfig.forName(name);
    }

    /**
     * Gets a component form a component key. A component key is formatted with two qualified component
     * names split by a period.
     *
     * @param key the component key.
     * @return the interface component for the component key or {@code null} if either the specified interface
     *          configuration or component does not exist.
     */
    public InterfaceComponent GetComponent(String key) {
        if(key.indexOf('.') == -1) throw new IllegalArgumentException("Invalid key format");
        String interfaceName = key.substring(0, key.indexOf('.')), componentName = key.substring(key.indexOf('.') + 1);
        return GetComponent(interfaceName, componentName);
    }

    /**
     *
     * @param interfaceName
     * @param componentName
     * @return
     */
    public InterfaceComponent GetComponent(String interfaceName, String componentName) {
        InterfaceConfig config = InterfaceConfig.forName(interfaceName);
        if(config == null) return null;

        return config.getComponent(componentName);
    }

    /**
     *
     * @param config
     * @param componentName
     * @return
     */
    public InterfaceComponent GetComponent(InterfaceConfig config, String componentName) {
        return config.getComponent(componentName);
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
        return "interface";
    }
}
