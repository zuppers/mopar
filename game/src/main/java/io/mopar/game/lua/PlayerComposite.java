package io.mopar.game.lua;

import io.mopar.core.lua.Coerce;
import io.mopar.game.model.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

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
     * @param inv
     * @param item
     * @param stack
     */
    public void add_item(LuaTable inv, ItemComposite item, boolean stack) {
        add_item(inv.get("id").checkint(), item, stack);
    }

    /**
     *
     * @param id
     * @param item
     * @param stack
     */
    public void add_item(int id, ItemComposite item, boolean stack) {
        player.addItem(id, item.getItem(), stack);
    }

    /**
     * Gives the player an item.
     *
     * @param inv the inventory table.
     * @param item the item configuration table.
     * @param amount the amount to give.
     */
    public void give_item(LuaTable inv, LuaTable item, int amount) {
        give_item(inv.get("id").checkint(), item.get("id").checkint(), amount, item.get("stackable").checkboolean());
    }

    /**
     * Gives the player an item.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     * @param amount the amount to give.
     * @param stack flag for if the items to give should stack in the inventory.
     */
    public void give_item(int id, int itemId, int amount, boolean stack) {
        player.giveItem(id, itemId, amount, stack);
    }

    /**
     *
     * @param table
     * @param slot
     * @return
     */
    public LuaValue get_item(LuaTable table, int slot) {
        return get_item(table.get("id").checkint(), slot);
    }

    /**
     *
     * @param source
     * @param dest
     * @param slot
     * @param stack
     */
    public void move_item(LuaTable source, LuaTable dest, int slot, boolean stack, boolean shift) {
        player.moveItem(source.get("id").checkint(), dest.get("id").checkint(), slot, stack, shift);
    }

    /**
     *
     * @param srcInv
     * @param destInv
     * @param firstSlot
     * @param secondSlot
     */
    public void swap_items(LuaTable srcInv, LuaTable destInv, int firstSlot, int secondSlot, boolean stack, boolean shift) {
        player.swapItems(srcInv.get("id").checkint(), destInv.get("id").checkint(), firstSlot, secondSlot, stack, shift);
    }

    /**
     *
     * @param inventory
     * @param first
     * @param second
     * @param mode
     */
    public void switch_items(LuaTable inventory, int first, int second, int mode) {
        switch_items(inventory.get("id").checkint(), first, second, mode);
    }

    /**
     *
     * @param id
     * @param first
     * @param second
     * @param mode
     */
    public void switch_items(int id, int first, int second, int mode) {
        player.swapItem(id, first, second, mode);
    }

    /**
     *
     * @param id
     * @param slot
     * @return
     */
    public LuaValue get_item(int id, int slot) {
        return Coerce.coerceToLua(player.getItem(id, slot));
    }

    /**
     *
     * @param inv
     * @param composite
     * @param stack
     * @return
     */
    public boolean accept_item(LuaTable inv, ItemComposite composite, boolean stack) {
        return player.acceptItem(inv.get("id").checkint(), composite.getItem(), inv.get("size").checkint(), stack);
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
    public void clear_inv(LuaTable inventory, LuaTable inter) {
        clear_inv(inventory.get("id").checkint(), inter.get("parent_id").checkint(), inter.get("id").checkint());
    }

    /**
     *
     * @param id
     * @param widgetId
     * @param componentId
     */
    public void clear_inv(int id, int widgetId, int componentId) {
        player.clearInventory(id, widgetId, componentId);
    }

    /**
     *
     * @param inventory
     * @param inter
     */
    public void refresh_inv(LuaTable inventory, LuaTable inter) {
        refresh_inv(inventory.get("id").checkint(), inter.get("parent_id").checkint(), inter.get("id").checkint());
    }

    /**
     *
     * @param id
     * @param widgetId
     * @param componentId
     */
    public void refresh_inv(int id, int widgetId, int componentId) {
        player.refreshInventory(id, widgetId, componentId);
    }

    /**
     *
     * @param inventory the inventory table.
     * @param inter the interface table.
     */
    public void update_inv(LuaTable inventory, LuaTable inter) {
        update_inv(inventory.get("id").checkint(), inter.get("parent_id").checkint(), inter.get("id").checkint());
    }

    /**
     *
     * @param id the inventory id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     */
    public void update_inv(int id, int widgetId, int componentId) {
        player.updateInventory(id, widgetId, componentId);
    }

    /**
     *
     */
    public void update_appearance() {
        player.setAppearanceUpdated(true);
    }

    public void play_song(int id) {
        player.playSong(id);
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
     *
     * @return
     */
    public int rights() { return player.getRights(); }

    /**
     * Prints text to the players submitPublicChat box.
     *
     * @param text the text to print.
     */
    public void print(String text) {
        player.print(text);
    }
}
