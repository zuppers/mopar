package io.mopar.game.lua;

import io.mopar.core.lua.LuaModule;

/**
 * @author Hadyn Fitzgerald
 */
public class OptionModule implements LuaModule {

    public static final int One     = 1;
    public static final int Two     = 2;
    public static final int Three   = 3;
    public static final int Four    = 4;
    public static final int Five    = 5;

    @Override
    public String getNamespace() {
        return "option";
    }
}
