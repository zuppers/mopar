package io.mopar.game.action;

import io.mopar.game.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 *
 * TODO(sinisoul): Possible way to handle player types from their current state.
 */
public class ActionBindings {

    /**
     * The numeric representation for no type.
     */
    public static final int NO_TYPE = -1;

    /**
     * The bound entity menu actions.
     */
    private Map<Integer, EntityMenuActionHandler<?>> entityMenuActions = new HashMap<>();

    /**
     * The bound button menu actions.
     */
    private Map<Long, ButtonMenuActionHandler> buttonMenuActions = new HashMap<>();

    /**
     *
     */
    private HashMap<Long, ItemMenuAction> itemMenuActions = new HashMap<>();

    /**
     * The swap item actions.
     */
    private Map<Integer, SwitchItemMenuAction> switchItemActions = new HashMap<>();

    /**
     * The command actions.
     */
    private Map<String, CommandAction> commandActions = new HashMap<>();

    private Map<Long, InterfaceItemMenuAction> interItemMenuActions = new HashMap<>();

    /**
     * Constructs a new {@link ActionBindings};
     */
    public ActionBindings() {}

    /**
     * Helper method; registers a player menu action.
     *
     * @param handler The action.
     * @param option The option to bind the action for.
     */
    public void registerPlayerMenuAction(EntityMenuActionHandler<Player> handler, int option) {
        registerEntityMenuAction(TargetType.PLAYER, handler, NO_TYPE, option);
    }

    /**
     * Registers an entity menu action.
     *
     * @param targetType The target type.
     * @param action The menu action.
     * @param typeId The type id.
     * @param option The menu option.
     * @throws IllegalArgumentException Thrown if the provided target type is not an entity subtype, if
     *                                  the provided type id is invalid, or if the option is out of range.
     */
    public void registerEntityMenuAction(TargetType targetType, EntityMenuActionHandler<?> action, int typeId, int option) {
        if(!TargetType.isEntityType(targetType)) {
            throw new IllegalArgumentException("Expected target type to be an entity subtype");
        }

        if(option < 0 || option > 10) {
            throw new IllegalArgumentException("Invalid menu option " + option);
        }

        // Check the type id, since players have no type we do not need to validate this.
        if(TargetType.PLAYER != targetType) {
            if(typeId < 0 || typeId >= 0x3fff) {
                throw new IllegalArgumentException("Invalid type id" + typeId);
            }
        }
        entityMenuActions.put(getEntityMenuActionKey(targetType, typeId, option), action);
    }

    /**
     *
     * @param action
     * @param widgetId
     * @param componentId
     * @param option
     */
    public void registerButtonMenuAction(ButtonMenuActionHandler action, int widgetId, int componentId, int option) {
        buttonMenuActions.put(getButtonMenuActionKey(widgetId, componentId, option), action);
    }


    /**
     *
     * @param action
     * @param widgetId
     * @param componentId
     * @param option
     */
    public void registerItemMenuAction(ItemMenuAction action, int widgetId, int componentId, int itemId, int option) {
        itemMenuActions.put(getItemMenuActionKey(widgetId, componentId, itemId, option), action);
    }

    /**
     *
     * @param action
     * @param widgetId
     * @param componentId
     * @param option
     */
    public void registerInterfaceItemMenuAction(InterfaceItemMenuAction action, int widgetId, int componentId, int option) {
        interItemMenuActions.put(getInterfaceItemMenuActionKey(widgetId, componentId, option), action);
    }

    /**
     *
     * @param action
     * @param widgetId
     * @param componentId
     */
    public void registerSwapItemAction(SwitchItemMenuAction action, int widgetId, int componentId) {
        switchItemActions.put(getSwitchItemActionKey(widgetId, componentId), action);
    }

    /**
     *
     * @param handler
     * @param command
     */
    public void registerCommandAction(CommandAction handler, String command) {
        commandActions.put(command, handler);
    }

    /**
     * Helper method; calls a player menu action.
     *
     * @param player The source player.
     * @param target The targeted player.
     * @param option The menu option.
     * @return If the action was successfully called.
     */
    public boolean callPlayerMenuAction(Player player, Player target, int option) {
        EntityMenuActionHandler<Player> action = (EntityMenuActionHandler<Player>) entityMenuActions.get(getEntityMenuActionKey(TargetType.PLAYER, NO_TYPE, option));
        if(action == null) {
            return false;
        }
        action.handle(player, target, option);
        return true;
    }

    /**
     *
     * @param player
     * @param widgetId
     * @param componentId
     * @param childId
     * @param option
     * @return
     */
    public boolean callButtonMenuAction(Player player, int widgetId, int componentId, int childId, int option) {
        ButtonMenuActionHandler action = buttonMenuActions.get(getButtonMenuActionKey(widgetId, componentId, option));
        if(action == null) {
            return false;
        }
        action.handle(player, widgetId, componentId, childId, option);
        return true;
    }

    /**
     *
     * @param player
     * @param widgetId
     * @param componentId
     * @param itemId
     * @param option
     * @return
     */
    public boolean callItemAction(Player player, int widgetId, int componentId, int itemId, int slot, int option) {
        ItemMenuAction action = itemMenuActions.get(getItemMenuActionKey(widgetId, componentId, itemId, option));
        if(action == null) {
            return false;
        }
        action.handle(player, itemId, slot);
        return true;
    }

    /**
     *
     * @param player
     * @param widgetId
     * @param componentId
     * @param itemId
     * @param slot
     * @param option
     * @return
     */
    public boolean callInterfaceItemAction(Player player, int widgetId, int componentId, int itemId, int slot, int option) {
        InterfaceItemMenuAction action = interItemMenuActions.get(getInterfaceItemMenuActionKey(widgetId, componentId, option));
        if(action == null) {
            return false;
        }
        action.handle(player, slot, itemId);
        return true;
    }

    /**
     *
     * @param player
     * @param widgetId
     * @param componentId
     * @param firstSlot
     * @param secondSlot
     * @param mode
     * @return
     */
    public boolean callSwitchItemAction(Player player, int widgetId, int componentId, int firstSlot, int secondSlot, int mode) {
        SwitchItemMenuAction action = switchItemActions.get(getSwitchItemActionKey(widgetId, componentId));
        if(action == null) {
            return false;
        }
        action.handle(player, firstSlot, secondSlot, mode);
        return true;
    }

    /**
     *
     * @param player
     * @param command
     * @param arguments
     * @return
     */
    public boolean callCommandAction(Player player, String command, String[] arguments) {
        CommandAction action = commandActions.get(command);
        if(action == null) {
            return false;
        }
        action.handle(player, command, arguments);
        return true;
    }

    /**
     * Gets an entity menu action key.
     *
     * @param targetType The target type.
     * @param typeId The type id.
     * @param option The menu option.
     * @return The hash key for the given parameters.
     */
    private static int getEntityMenuActionKey(TargetType targetType, int typeId, int option) {
        return (typeId & 0x3fff) << 7 | (option & 0xf) << 3 | targetType.ordinal();
    }

    /**
     *
     * @param widgetId
     * @param componentId
     * @param option
     * @return
     */
    private long getButtonMenuActionKey(int widgetId, int componentId, int option) {
        return (option & 0xfL) << 32L | (widgetId & 0xffffL) << 16L | (componentId & 0xffffL);
    }

    /**
     *
     * @param widgetId
     * @param componentId
     * @return
     */
    private int getSwitchItemActionKey(int widgetId, int componentId) {
        return (widgetId & 0xffff) << 16 | (componentId & 0xffff);
    }

    /**
     *
     * @param widgetId
     * @param componentId
     * @param option
     * @return
     */
    private long getItemMenuActionKey(int widgetId, int componentId, int itemId, int option) {
        return (option & 0xfL) << 48L | (itemId & 0xffffL) << 32L | (widgetId & 0xffffL) << 16 | (componentId & 0xffffL);
    }

    /**
     *
     * @param widgetId
     * @param componentId
     * @param option
     * @return
     */
    private long getInterfaceItemMenuActionKey(int widgetId, int componentId, int option) {
        return (option & 0xfL) << 32L | (widgetId & 0xffffL) << 16 | (componentId & 0xffffL);
    }
}