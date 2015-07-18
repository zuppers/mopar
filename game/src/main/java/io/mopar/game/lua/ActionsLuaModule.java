package io.mopar.game.lua;

import io.mopar.game.action.ActionBindings;
import io.mopar.game.action.TargetType;
import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaNumber;

/**
 * @author Hadyn Fitzgerald
 */
public class ActionsLuaModule implements LuaModule {

    /**
     * The action bindings.
     */
    private ActionBindings bindings;

    /**
     * Constructs a new {@link ActionsLuaModule};
     *
     * @param bindings The action bindings.
     */
    public ActionsLuaModule(ActionBindings bindings) {
        this.bindings = bindings;
    }

    /**
     * Binds a menu option action.
     *
     * @param target The target.
     * @param option The menu option.
     * @param closure The lua closure to wrap the action with.
     */
    public void on_option(String target, int option, LuaClosure closure) {
        on_option(target, ActionBindings.NO_TYPE, option, closure);
    }

    /**
     * Binds a menu option action.
     *
     * @param target The target.
     * @param typeId The target type id.
     * @param option The menu option.
     * @param closure The lua closure to wrap the action with.
     */
    public void on_option(String target, int typeId, int option, LuaClosure closure) {
        bindings.registerEntityMenuAction(TargetType.valueOf(target.toUpperCase()),
                (player, entity, opt) -> closure.invoke(
                        Coerce.coerceToLua(player),
                        Coerce.coerceToLua(entity),
                        LuaNumber.valueOf(opt)),
                typeId, option);
    }

    /**
     * Gets the module namespace.
     *
     * @return The namespace.
     */
    @Override
    public String getNamespace() {
        return "action";
    }
}
