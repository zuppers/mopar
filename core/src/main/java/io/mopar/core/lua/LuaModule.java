package io.mopar.core.lua;

import org.luaj.vm2.LuaTable;

/**
 * @author Hadyn Fitzgerald
 *
 * Lua module methods are expected to follow conventions.
 */
public interface LuaModule {

    /**
     * Gets the module meta table.
     *
     * @return the meta table.
     */
    default LuaTable getMetaTable() {
        return null;
    }

    /**
     * Gets the namespace for the module.
     *
     * @return The namespace.
     */
    String getNamespace();
}
