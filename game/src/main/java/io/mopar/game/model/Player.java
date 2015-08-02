package io.mopar.game.model;

import io.mopar.account.InventoryModel;
import io.mopar.account.ItemModel;
import io.mopar.account.Profile;
import io.mopar.account.SkillModel;
import io.mopar.core.msg.Message;
import io.mopar.core.msg.MessageListener;
import io.mopar.game.model.block.RegionSet;
import io.mopar.game.msg.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Hadyn Fitzgerald
 */
public class Player extends Mobile {

    /**
     * The username.
     */
    private long username;

    /**
     * The inventories.
     */
    private Map<Integer, Inventory> inventories = new HashMap<>();

    /**
     * The message listeners.
     */
    private List<MessageListener> messageListeners = new ArrayList<>();

    /**
     * The appearance.
     */
    private Appearance appearance = new Appearance();

    /**
     *
     */
    private boolean appearanceUpdated;

    /**
     * The display mode.
     */
    private int displayMode;

    /**
     * The scene.
     */
    private Scene scene = new Scene();

    /**
     * The submitPublicChat message.
     */
    private ChatMessage publicChatMessage;

    /**
     *
     */
    private SkillSet skills = new SkillSet();

    /**
     * Constructs a new {@link Player};
     */
    public Player() {}

    /**
     *
     * @param username
     */
    public void setUsername(long username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public long getUid() {
        return username;
    }

    /**
     * Sets the display mode.
     *
     * @param displayMode The display mode.
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
     * Gets the players current display mode.
     *
     * @return the display mode.
     */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
     * Gets the rights.
     *
     * @return the rights.
     */
    public int getRights() {
        return 2;
    }

    /**
     *
     * @param id
     * @param active
     */
    public void setFeatureVisible(int id, boolean active) {
        appearance.setVisible(id, active);
    }

    /**
     *
     * @param bool
     */
    public void setAppearanceUpdated(boolean bool) {
        this.appearanceUpdated = bool;
    }

    /**
     *
     * @return
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Gets if the appearance was updated.
     *
     * @return if the appearnace was updated.
     */
    public boolean getAppearanceUpdated() {
        return appearanceUpdated;
    }

    /**
     *
     * @param id
     * @param item
     * @param stack
     */
    public void addItem(int id, Item item, boolean stack) {
        Inventory inventory = getInventory(id);
        inventory.add(item, stack);
    }

    /**
     *
     * @param id
     * @param item
     * @param size
     * @param stack
     * @return
     */
    public boolean acceptItem(int id, Item item, int size, boolean stack) {
        if(!hasInventory(id)) {
            return size > 0;
        }
        Inventory inventory = inventories.get(id);
        return inventory.accept(item, size, stack);
    }

    /**
     * Helper method; adds an item to an inventory.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     * @param amount the amount of the item.
     * @param stack if the item should stack.
     */
    public void giveItem(int id, int itemId, int amount, boolean stack) {
        Inventory inventory = getInventory(id);
        inventory.add(itemId, amount, stack);
    }

    /**
     * Helper method; moves an item from one inventory to another.
     *  @param srcId the source inventory id.
     * @param destId the destination inventory id.
     * @param slot the source inventory slot.
     * @param stack if the item should stack on addition to the destination inventory.
     * @param shift if the items should shift after removal from the source inventory.
     */
    public void moveItem(int srcId, int destId, int slot, boolean stack, boolean shift) {
        if(!hasInventory(srcId)) {
            return;
        }

        Inventory inventory = inventories.get(srcId);
        Item item = inventory.remove(slot, shift);

        Inventory dest = getInventory(destId);
        dest.add(item.getId(), item.getAmount(), stack);
    }

    /**
     * Helper method; gets the count of an item in an inventory.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     */
    public int countItem(int id, int itemId) {
        if(!hasInventory(id)) {
            return 0;
        }
        Inventory inventory = inventories.get(id);
        return inventory.count(itemId);
    }

    /**
     * Helper method, swaps items in an inventory.
     *
     * @param id the inventory id.
     * @param firstSlot the first slot.
     * @param secondSlot the second slot.
     * @param mode the swap mode.
     */
    public void swapItem(int id, int firstSlot, int secondSlot, int mode) {
        if(!hasInventory(id)) {
            return;
        }
        Inventory inventory = inventories.get(id);
        inventory.swap(firstSlot, secondSlot, mode);
    }

    /**
     *
     * @param sourceId
     * @param destId
     * @param firstSlot
     * @param secondSlot
     * @param stack
     * @param shift
     */
    public void swapItems(int sourceId, int destId, int firstSlot, int secondSlot, boolean stack, boolean shift) {
        if(!hasInventory(sourceId)) {
            return;
        }
        Inventory source = inventories.get(sourceId);
        Item item = source.remove(firstSlot, shift);

        Inventory dest = getInventory(destId);
        Item replace = dest.get(secondSlot);
        if(replace != null && replace.getId() == item.getId() && stack) {
            int amount = item.getAmount();
            if(item.getAmount() + replace.getAmount() < 0) {
                amount = Integer.MAX_VALUE - replace.getAmount();
                source.set(firstSlot, new Item(item.getId(), item.getAmount() - amount));
            } else {
                amount += replace.getAmount();
            }
            dest.set(secondSlot, new Item(item.getId(), amount));
        } else {
            source.set(firstSlot, replace);
            dest.set(secondSlot, item);
        }
    }

    /**
     *
     * @param id
     * @param slot
     * @return
     */
    public Item getItem(int id, int slot) {
        if(!hasInventory(id)) {
            return null;
        }
        return inventories.get(id).get(slot);
    }

    /**
     * Helper method; removes an item from an inventory.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     * @param amount the amount to remove.
     * @param shift if the items should be shifted after removal.
     */
    public void removeItem(int id, int itemId, int amount, boolean shift) {
        if(!hasInventory(id)) {
            return;
        }
        Inventory inventory = inventories.getOrDefault(id, new Inventory());
        inventory.remove(itemId, amount, shift);
    }

    /**
     * Gets the space for an item in an inventory.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     * @param amount the amount of the item.
     * @param limit the inventory size limit.
     * @param stackable flag for if the item is stackable.
     * @return the amount of the item that the inventory can accept, up to the specified amount.
     */
    public int getInventorySpace(int id, int itemId, int amount, int limit, boolean stackable) {
        Inventory inventory = getInventory(id);
        return inventory.getSpace(itemId, amount, limit, stackable);
    }

    /**
     * Updates an inventory.
     *
     * @param id the inventory id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     */
    public void updateInventory(int id, int widgetId, int componentId) {
        if(!hasInventory(id)) {
            return;
        }
        Inventory inventory = inventories.get(id);
        UpdateInventoryMessage message = new UpdateInventoryMessage(inventory, id, widgetId, componentId);
        inventory.getUpdatedSlots().forEach(message::addSlot);
        inventory.resetUpdatedSlots();
        send(message);
    }

    /**
     * Refreshes an inventory. If the inventory does not exist for the specified id
     * then the inventory is cleared.
     *
     * @param id the inventory id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     */
    public void refreshInventory(int id, int widgetId, int componentId) {
        send(new RefreshInventoryMessage(getInventory(id), id, widgetId, componentId));
    }

    /**
     * Clears an inventory.
     *
     * @param id the inventory id.
     * @param widgetId the widget id.
     * @param componentId the component id.
     */
    public void clearInventory(int id, int widgetId, int componentId) {
        Inventory inventory = getInventory(id);
        inventory.clear();
        refreshInventory(id, widgetId, componentId);
    }

    /**
     * Gets or creates an inventory for the specified id if it does not exist.
     *
     * @param id the inventory id.
     * @return the inventory for the id.
     */
    public Inventory getInventory(int id) {
        Inventory inventory = inventories.getOrDefault(id, new Inventory());
        inventories.putIfAbsent(id, inventory);
        return inventory;
    }

    /**
     * Gets if an inventory has been initialized for a player.
     *
     * @param id the inventory id.
     * @return if the inventory has been initialized.
     */
    public boolean hasInventory(int id) {
        return inventories.containsKey(id);
    }

    /**
     * Sets the public submitPublicChat message.
     *
     * @param message the public submitPublicChat message.
     */
    public void setPublicChatMessage(ChatMessage message) {
        this.publicChatMessage = message;
    }

    /**
     * Gets the public submitPublicChat message.
     *
     * @return the public submitPublicChat message.
     */
    public ChatMessage getPublicChatMessage() {
        return publicChatMessage;
    }

    /**
     * Gets if the public submitPublicChat message is set.
     *
     * @return if the public submitPublicChat mess is set.
     */
    public boolean hasPublicChatMessage() {
        return publicChatMessage != null;
    }

    /**
     *
     * @param widgetId
     */
    public void setRootInterface(int widgetId) {
        send(new SetRootInterfaceMessage(widgetId));
    }

    /**
     *
     * @param parentId
     * @param componentId
     * @param widgetId
     * @param type
     */
    public void setInterface(int parentId, int componentId, int widgetId, int type) {
        send(new SetInterfaceMessage(parentId, componentId, widgetId, type));
    }

    /**
     *
     * @param widgetId
     * @param componentId
     * @param text
     */
    public void setInterfaceText(int widgetId, int componentId, String text) {
        send(new SetInterfaceTextMessage(widgetId, componentId, text));
    }


    /**
     * Gets the scene.
     *
     * @return the scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Updates the local player graph.
     *
     * @param players The player list.
     */
    public void updateLocalPlayers(EntityList<Player> players) {
        scene.updateLocalPlayers(players, getPosition());
        send(PlayerSynchronizationMessage.create(this));
    }

    /**
     *
     * @param npcs
     */
    public void updateLocalNpcs(EntityList<NPC> npcs) {
        scene.updateLocalNpcs(npcs, getPosition());
        send(NpcSynchronizationMessage.create(this));
    }

    /**
     *
     * @param regions
     */
    public void updateBlocks(RegionSet regions) {
        scene.updateBlocks(regions, getPosition());
        send(BlockSynchronizationMessage.create(this, regions));
    }

    /**
     * Gets if the scene needs to be built using the current position
     * to check the condition.
     *
     * @return if the scene needs to be built.
     */
    public boolean getRebuildScene() {
        return scene.checkRebuild(getPosition());
    }

    /**
     * Builds the scene at the current position.
     */
    public void rebuildScene() {
        scene.build(getPosition());
        send(new RebuildSceneMessage(getPosition()));
    }

    /**
     * Prints text to the players console.
     *
     * @param text
     */
    public void print(String text) {
        send(new PrintMessage(text));
    }

    /**
     * Adds a message listener.
     *
     * @param listener The message listener.
     */
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     *
     * @param messages
     */
    public void send(Collection<? extends Message> messages) {
        messages.forEach(this::send);
    }

    /**
     * Sends a message.
     *
     * @param message The message to send.
     */
    public void send(Message message) {
        messageListeners.forEach(listener -> listener.handle(message));
    }

    /**
     *
     * @param skill
     * @param amount
     */
    public void giveExp(int skill, double amount) {
        skills.giveExperience(skill, amount);
        Double d = Double.valueOf(skills.getExperience(skill));
        send(new UpdateSkillMessage(skill, d.intValue(), skills.getStat(skill)));
    }

    /**
     *
     */
    public void refreshSkills() {
        for(int i = 0; i < Skills.COUNT; i++) {
            send(new UpdateSkillMessage(i, Double.valueOf(skills.getExperience(i)).intValue(), skills.getStat(i)));
        }
    }

    /**
     *
     * @return
     */
    public SkillSet getSkills() {
        return skills;
    }

    /**
     *
     * @param id
     */
    public void playSong(int id) {
        send(new PlaySongMessage(id));
    }

    /**
     * Resets the player.
     */
    public void reset() {
        publicChatMessage = null;
        appearanceUpdated = false;
        super.reset();
    }

    /**
     *
     * @return
     */
    public Profile toProfile() {
        Profile profile = new Profile();
        profile.setUid(username);

        profile.setX(getPosition().getX());
        profile.setY(getPosition().getY());
        profile.setPlane(getPosition().getPlane());

        for(int i = 0; i < Skills.COUNT; i++) {
            SkillModel skill = new SkillModel();
            skill.setId(i);
            skill.setStat(skills.getStat(i));
            skill.setExperience(skills.getExperience(i));

            profile.addSkill(skill);
        }

        for(Entry<Integer, Inventory> entry : inventories.entrySet()) {
            Inventory inventory = entry.getValue();

            InventoryModel model = new InventoryModel();
            model.setId(entry.getKey());

            for(int i = 0; i < inventory.capacity(); i++) {
                Item item = inventory.get(i);
                if(item == null) continue;

                ItemModel itemModel = new ItemModel();
                itemModel.setId(item.getId());
                itemModel.setAmount(item.getAmount());
                itemModel.setSlot(i);

                model.addItem(itemModel);
            }

            profile.addInventory(model);
        }

        return profile;
    }
}
