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
    private Map<Integer, EntityMenuAction<?>> entityMenuActions = new HashMap<>();

    /**
     * The bound button menu actions.
     */
    private Map<Long, ButtonMenuAction> buttonMenuActions = new HashMap<>();

    /**
     * Constructs a new {@link ActionBindings};
     */
    public ActionBindings() {}

    /**
     * Helper method; registers a player menu action.
     *
     * @param action The menu action.
     * @param option The option to bind the action for.
     */
    public void registerPlayerMenuAction(EntityMenuAction<Player> action, int option) {
        registerEntityMenuAction(TargetType.PLAYER, action, NO_TYPE, option);
    }

    /**
     *
     * @param action
     * @param widgetId
     * @param componentId
     * @param option
     */
    public void registerButtonMenuAction(ButtonMenuAction action, int widgetId, int componentId, int option) {
        buttonMenuActions.put(getButtonMenuActionKey(widgetId, componentId, option), action);
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
        EntityMenuAction<Player> action = (EntityMenuAction<Player>) entityMenuActions.get(getEntityMenuActionKey(TargetType.PLAYER, NO_TYPE, option));
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
        ButtonMenuAction action = buttonMenuActions.get(getButtonMenuActionKey(widgetId, componentId, option));
        if(action == null) {
            return false;
        }
        action.handle(player, widgetId, componentId, childId, option);
        return true;
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
    public void registerEntityMenuAction(TargetType targetType, EntityMenuAction<?> action, int typeId, int option) {
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
        return (option & 0xfL) << 32L | (widgetId & 0xffffL) << 16L | (componentId & 0xffff);
    }
}
