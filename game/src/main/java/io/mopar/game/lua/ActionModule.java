package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import io.mopar.game.action.ActionBindings;
import io.mopar.game.config.InterfaceComponent;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;

/**
 * @author Hadyn Fitzgerald
 */
public class ActionModule implements LuaModule {

    /**
     * The action bindings.
     */
    private ActionBindings bindings;

    /**
     * Constructs a new {@link ActionModule};
     *
     * @param bindings the action bindings.
     */
    public ActionModule(ActionBindings bindings) {
        this.bindings = bindings;
    }

    /**
     *
     * @param name
     * @param closure
     */
    public void OnCommand(String name, LuaClosure closure) {
        bindings.registerCommandAction(name, (player, command, arguments) ->
            closure.invoke(new LuaValue[]{
                    Coerce.coerceToLua(player),
                    Coerce.coerceToLua(command),
                    Coerce.coerceToLua(arguments)
            }));
    }

    /**
     *
     * @param component
     * @param option
     * @param closure
     */
    public void OnButton(InterfaceComponent component, int option, LuaClosure closure) {
        OnButton(component.getParentId(), component.getId(), option, closure);
    }

    /**
     *
     * @param interfaceId
     * @param componentId
     * @param option
     * @param closure
     */
    public void OnButton(int interfaceId, int componentId, int option, LuaClosure closure) {
        bindings.registerButtonMenuAction(interfaceId, componentId, option, (plr, widget, comp, child, opt) ->
                closure.invoke(new LuaValue[]{
                        Coerce.coerceToLua(plr),
                        Coerce.coerceToLua(child)
                }));
    }

    /**
     * Registers an action handler for an item menu option activated on an interface.
     *
     * @param interfaceId the interface id.
     * @param componentId the interface component id.
     * @param itemId the item configuration id.
     * @param option the option id.
     * @param closure the closure.
     */
    public void OnItemOption(int interfaceId, int componentId, int itemId, int option, LuaClosure closure) {
        bindings.registerItemMenuAction(interfaceId, componentId, itemId, option, (player, id, slot) ->
                closure.invoke(new LuaValue[]{
                        Coerce.coerceToLua(player),
                        Coerce.coerceToLua(id),
                        Coerce.coerceToLua(slot)
                }));
    }

    /**
     *
     * @param component
     * @param closure
     */
    public void OnSwitchItems(InterfaceComponent component, LuaClosure closure) {
        OnSwitchItems(component.getParentId(), component.getId(), closure);
    }

    /**
     *
     * @param interfaceId
     * @param componentId
     * @param closure
     */
    public void OnSwitchItems(int interfaceId, int componentId, LuaClosure closure) {
        bindings.registerSwitchItemsAction(interfaceId, componentId, ((player, firstSlot, secondSlot, mode) -> closure.invoke(new LuaValue[]{
                Coerce.coerceToLua(player),
                Coerce.coerceToLua(firstSlot),
                Coerce.coerceToLua(secondSlot),
                Coerce.coerceToLua(mode)
        })));
    }

    /**
     * Gets the namespace.
     *
     * @return the namespace.
     */
    @Override
    public String getNamespace() {
        return "action";
    }
}
