package io.mopar.game.model;

import io.mopar.core.msg.Message;
import io.mopar.core.msg.MessageListener;
import io.mopar.game.msg.*;

import java.util.*;

/**
 * @author Hadyn Fitzgerald
 */
public class Player extends Mobile {

    /**
     * The inventories.
     */
    private Map<Integer, Inventory> inventories = new HashMap<>();

    /**
     * The message listeners.
     */
    private List<MessageListener> messageListeners = new ArrayList<>();

    /**
     * The display mode.
     */
    private int displayMode;

    /**
     * The scene.
     */
    private Scene scene = new Scene();

    /**
     * The chat message.
     */
    private ChatMessage publicChatMessage;

    /**
     * Constructs a new {@link Player};
     */
    public Player() {}

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
     * Adds an item to an inventory.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     * @param amount the amount of the item.
     * @param stack if the item should stack.
     */
    public void addItem(int id, int itemId, int amount, boolean stack) {
        Inventory inventory = getInventory(id);
        inventory.add(itemId, amount, stack);
    }

    /**
     * Moves an item from one inventory to another.
     *
     * @param srcId the source inventory id.
     * @param destId the destination inventory id.
     * @param slot the source inventory slot.
     * @param shift if the items should shift after removal from the source inventory.
     * @param stack if the item should stack on addition to the destination inventory.
     */
    public void moveItem(int srcId, int destId, int slot, boolean shift, boolean stack) {
        if(!hasInventory(srcId)) {
            return;
        }

        Inventory inventory = inventories.get(srcId);
        Item item = inventory.remove(slot, shift);

        Inventory dest = getInventory(destId);
        dest.add(item.getId(), item.getAmount(), stack);
    }

    /**
     * Gets the count of an item in an inventory.
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
     *
     * @param id
     * @param first
     * @param second
     * @param mode
     */
    public void swapItem(int id, int first, int second, int mode) {
        if(!hasInventory(id)) {
            return;
        }
        Inventory inventory = inventories.get(id);
        inventory.swap(first, second, mode);
    }

    /**
     * Removes an item from an inventory.
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
    private Inventory getInventory(int id) {
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
     *
     * @param message
     */
    public void setPublicChatMessage(ChatMessage message) {
        this.publicChatMessage = message;
        setUpdated(true);
    }


    /**
     * Gets the public chat message.
     *
     * @return the public chat message.
     */
    public ChatMessage getPublicChatMessage() {
        return publicChatMessage;
    }

    /**
     * Gets if the public chat message is set.
     *
     * @return if the public chat mess is set.
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
     * Sends a message.
     *
     * @param message The message to send.
     */
    public void send(Message message) {
        messageListeners.forEach(listener -> listener.handle(message));
    }

    /**
     * Resets the player.
     */
    public void reset() {
        publicChatMessage = null;
        super.reset();
    }
}
