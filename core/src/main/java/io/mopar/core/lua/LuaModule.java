package io.mopar.core.lua;

/**
 * @author Hadyn Fitzgerald
 *
 * TODO(sinisoul): Need to figure out how register module constants.
 *
 * Lua module methods are expected to follow lua convention styles. Either snakecase or
 * alllowercase.
 */
public interface LuaModule {

    /**
     * Gets the namespace for the module.
     *
     * @return The namespace.
     */
    String getNamespace();
}
