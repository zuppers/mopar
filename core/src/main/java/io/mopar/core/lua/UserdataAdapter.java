package io.mopar.core.lua;

import org.luaj.vm2.LuaTable;

/**
 * Created by hadyn on 6/22/15.
 */
public interface UserdataAdapter {

    default LuaTable getMetaTable() {
        return null;
    }
}
