package io.mopar.game.lua.model;

import io.mopar.core.lua.UserdataAdapter;
import io.mopar.game.config.InterfaceComponent;
import io.mopar.game.config.InterfaceConfig;
import io.mopar.game.config.InventoryConfig;
import io.mopar.game.config.SongConfig;
import io.mopar.game.model.DisplayMode;
import io.mopar.game.model.Player;
import io.mopar.game.model.Skills;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerAdapter extends MobileAdapter implements UserdataAdapter {

    /**
     * The player.
     */
    private Player player;

    /**
     * Constructs a new {@link PlayerAdapter};
     *
     * @param player the player.
     */
    public PlayerAdapter(Player player) {
        super(player);
        this.player = player;
    }

    /**
     * Gets the rights.
     *
     * @return the rights.
     */
    public int GetRights() {
        return player.getRights();
    }

    /**
     * Gets the current display mode.
     *
     * @return the display mode.
     *
     * @see DisplayMode
     */
    public int GetDisplayMode() {
        return player.getDisplayMode();
    }

    /**
     * Opens a screen.
     *
     * @param config the interface to open.
     */
    public void OpenScreen(InterfaceConfig config) {
        player.openScreen(config.getId());
    }

    /**
     * Opens an interface.
     *
     * @param target the target interface component.
     * @param config the interface to open.
     * @param mode
     */
    public void OpenInterface(InterfaceComponent target, InterfaceConfig config, int mode) {
        OpenInterface(target.getParentId(), target.getId(), config.getId(), mode);
    }

    /**
     * Opens an interface.
     *
     * @param parentId the parent interface id.
     * @param componentId the parent component id.
     * @param interfaceId the id of the interface to open.
     * @param mode
     */
    public void OpenInterface(int parentId, int componentId, int interfaceId, int mode) {
        player.openInterface(parentId, componentId, interfaceId, mode);
    }

    /**
     * Sets the text of an interface.
     *
     * @param component the component.
     * @param text the interface text.
     */
    public void SetInterfaceText(InterfaceComponent component, String text) {
        SetInterfaceText(component.getParentId(), component.getId(), text);
    }

    /**
     * Sets the text of an interface.
     *
     * @param interfaceId the interface id.
     * @param componentId the component id.
     * @param text the interface text.
     */
    public void SetInterfaceText(int interfaceId, int componentId, String text) {
        player.setInterfaceText(interfaceId, componentId, text);
    }

    /**
     * Sets if an interface is visible.
     *
     * @param interfaceId the interface id.
     * @param componentId the component id.
     * @param visible the visible flag.
     */
    public void SetInterfaceVisible(int interfaceId, int componentId, boolean visible) {
        player.setInterfaceHidden(interfaceId, componentId, visible);
    }

    /**
     * Switches items in an inventory.
     *
     * @param config the inventory configuration.
     * @param firstSlot the first slot.
     * @param secondSlot the second slot.
     * @param mode the .
     */
    public void SwitchItems(InventoryConfig config, int firstSlot, int secondSlot, int mode) {
        SwitchItems(config.getId(), firstSlot, secondSlot, mode);
    }

    /**
     * Switches items in an inventory.
     *
     * @param id the inventory id.
     * @param firstSlot the first slot.
     * @param secondSlot the second slot.
     * @param mode the mode.
     */
    public void SwitchItems(int id, int firstSlot, int secondSlot, int mode) {
        player.switchItems(id, firstSlot, secondSlot, mode);
    }

    /**
     * Updates an inventory.
     *
     * @param config the inventory config.
     * @param component the interface component.
     */
    public void UpdateInventory(InventoryConfig config, InterfaceComponent component) {
        UpdateInventory(config.getId(), component.getParentId(), component.getId());
    }

    /**
     * Updates an inventory.
     *
     * @param id the inventory id.
     * @param interfaceId the interface id.
     * @param componentId the component id.
     */
    public void UpdateInventory(int id, int interfaceId, int componentId) {
        player.updateInventory(id, interfaceId, componentId);
    }

    /**
     * Refreshes an inventory.
     *
     * @param config the inventory config.
     * @param component the interface component.
     */
    public void RefreshInventory(InventoryConfig config, InterfaceComponent component) {
        RefreshInventory(config.getId(), component.getParentId(), component.getId());
    }

    /**
     * Refreshes an inventory.
     *
     * @param id the inventory id.
     * @param interfaceId the interface id.
     * @param componentId the component id.
     */
    public void RefreshInventory(int id, int interfaceId, int componentId) {
        player.refreshInventory(id, interfaceId, componentId);
    }

    /**
     * Gives the player experience in a skill.
     *
     * @param skillId the skill id.
     * @param amount the amount of experience to give.
     */
    public void GiveExperience(int skillId, double amount) {
        player.addExperience(skillId, amount);
    }

    /**
     * Gets the stat for a skill.
     *
     * @param skillId the skill id.
     * @return the stat for the provided skill id.
     *
     * @see Skills
     */
    public int GetStat(int skillId) {
        return player.getStat(skillId);
    }

    /**
     * Gets the level for a skill.
     *
     * @param skillId the skill id.
     * @return the level for the provided skill id.
     *
     * @see Skills
     */
    public int GetLevel(int skillId) {
        return player.getLevel(skillId);
    }

    /**
     * Refreshes all of the player skills.
     */
    public void RefreshSkills() {
        player.refreshSkills();
    }

    /**
     * Plays a song.
     *
     * @param config the song configuration.
     */
    public void PlaySong(SongConfig config) {
        PlaySong(config.getFileId());
    }

    /**
     * Plays a song.
     *
     * @param id the file id.
     */
    public void PlaySong(int id) {
        player.playSong(id);
    }

    /**
     * Rebuilds the scene.
     */
    public void RebuildScene() {
        player.rebuildScene();
    }

    /**
     * Prints a message in the players chat box.
     *
     * @param text the text to print.
     */
    public void Print(String text) {
        player.print(text);
    }
}
