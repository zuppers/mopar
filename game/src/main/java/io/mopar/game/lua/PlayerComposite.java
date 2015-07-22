package io.mopar.game.lua;

import io.mopar.game.model.Player;
import org.luaj.vm2.LuaTable;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerComposite extends MobileComposite {

    /**
     * The player.
     */
    private Player player;

    /**
     * Constructs a new {@link PlayerComposite};
     *
     * @param player The player.
     */
    public PlayerComposite(Player player) {
        super(player);
        this.player = player;
    }

    /**
     *
     * @param inventory
     * @param item
     * @param amount
     */
    public void inv_add(LuaTable inventory, LuaTable item, int amount) {
        inv_add(inventory.get("id").checkint(), item.get("id").checkint(), amount, item.get("stackable").checkboolean());
    }

    /**
     *
     * @param id
     * @param itemId
     * @param amount
     * @param stackable
     */
    public void inv_add(int id, int itemId, int amount, boolean stackable) {
        player.addItem(id, itemId, amount, stackable);
    }

    /**
     *
     * @param inventory
     * @param first
     * @param second
     * @param mode
     */
    public void inv_swap(LuaTable inventory, int first, int second, int mode) {
        inv_swap(inventory.get("id").checkint(), first, second, mode);
    }

    /**
     *
     * @param id
     * @param first
     * @param second
     * @param mode
     */
    public void inv_swap(int id, int first, int second, int mode) {
        player.swapItem(id, first, second, mode);
    }

    /**
     *
     * @param inventory
     * @param item
     * @return
     */
    public int inv_space(LuaTable inventory, LuaTable item, int amount) {
        return player.getInventorySpace(inventory.get("id").checkint(), item.get("id").checkint(), amount,
                inventory.get("size").checkint(), item.get("stackable").checkboolean());
    }

    /**
     *
     * @param inventory
     * @param inter
     */
    public void inv_clear(LuaTable inventory, LuaTable inter) {
        inv_clear(inventory.get("id").checkint(), inter.get("parent_id").checkint(), inter.get("id").checkint());
    }

    /**
     *
     * @param id
     * @param widgetId
     * @param componentId
     */
    public void inv_clear(int id, int widgetId, int componentId) {
        player.clearInventory(id, widgetId, componentId);
    }

    /**
     *
     * @param inventory
     * @param inter
     */
    public void inv_refresh(LuaTable inventory, LuaTable inter) {
        inv_refresh(inventory.get("id").checkint(), inter.get("parent_id").checkint(), inter.get("id").checkint());
    }

    /**
     *
     * @param id
     * @param widgetId
     * @param componentId
     */
    public void inv_refresh(int id, int widgetId, int componentId) {
        player.refreshInventory(id, widgetId, componentId);
    }

    /**
     *
     * @param inventory the inventory table.
     * @param inter the interface table.
     */
    public void inv_update(LuaTable inventory, LuaTable inter) {
        inv_update(inventory.get("id").checkint(), inter.get("parent_id").checkint(), inter.get("id").checkint());
    }

    /**
     *
     * @param id the inventory id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     */
    public void inv_update(int id, int widgetId, int componentId) {
        player.updateInventory(id, widgetId, componentId);
    }

    /**
     *
     *
     * @param table
     */
    public void set_root_interface(LuaTable table) {
        set_root_interface(table.get("id").checkint());
    }

    /**
     *
     * @param widgetId
     */
    public void set_root_interface(int widgetId) {
        player.setRootInterface(widgetId);
    }

    /**
     *
     * @param component
     * @param table
     */
    public void set_interface(LuaTable component, LuaTable table, int type) {
        player.setInterface(component.get("parent_id").checkint(), component.get("id").checkint(),
                table.get("id").checkint(), type);
    }

    /**
     *
     * @param parentId
     * @param componentId
     * @param widgetId
     * @param type
     */
    public void set_interface(int parentId, int componentId, int widgetId, int type) {
        player.setInterface(parentId, componentId, widgetId, type);
    }

    /**
     * Gets the display mode.
     *
     * @return the display mode.
     */
    public int display_mode() {
        return player.getDisplayMode();
    }

    /**
     * Prints text to the players chat box.
     *
     * @param text the text to print.
     */
    public void print(String text) {
        player.print(text);
    }
}
