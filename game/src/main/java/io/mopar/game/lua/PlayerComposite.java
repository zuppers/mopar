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
     *
     * @param table
     */
    public void set_root_interface(LuaTable table) {
        set_root_interface(table.get("id").checkint());
    }

    /**
     *
     * @param i
     */
    public void set_root_interface(int i) {
        player.setRootInterface(i);
    }

    /**
     *
     * @param component
     * @param table
     */
    public void set_interface(LuaTable component, LuaTable table, int a) {
        player.setInterface(component.get("parent_id").checkint(), component.get("id").checkint(), table.get("id").checkint(), a);
    }

    public void set_interface(int i, int i1, int i2, int i3) {
        player.setInterface(i, i1, i2, i3);
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
