package io.mopar.game.model;

import io.mopar.account.*;
import io.mopar.core.Base37;
import io.mopar.core.msg.Message;
import io.mopar.core.msg.MessageListener;
import io.mopar.game.config.ChildActions;
import io.mopar.game.config.InterfaceComponent;
import io.mopar.game.config.InterfaceConfig;
import io.mopar.game.config.InventoryConfig;
import io.mopar.game.model.block.RegionSet;
import io.mopar.game.msg.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Hadyn Fitzgerald
 */
public class Player extends Mobile {

    /**
     * The user identifier, alias for username.
     */
    private long uid;

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
     * Flag for if the appearance has been updated.
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
     * The most recently published public chat message.
     */
    private ChatMessage publicChatMessage;

    /**
     * The skills.
     */
    private SkillSet skills = new SkillSet();

    /**
     * The interfaces.
     */
    private InterfaceSet interfaces = new InterfaceSet();

    /**
     * Constructs a new {@link Player};
     */
    public Player() {}

    /**
     * Sets the unique identifier.
     *
     * @param uid the unique identifier or username.
     */
    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * Gets the unique identifier.
     *
     * @return the unique identifier.
     */
    public long getUid() {
        return uid;
    }

    /**
     * Gets the username of the player.
     *
     * @return the username,
     */
    public String getUsername() {
        return Base37.decode(uid);
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
     * Sets the value of a variable.
     *
     * @param id the variable id.
     * @param value the value.
     */
    @Override
    public void setVariable(int id, int value) {
        super.setVariable(id, value);
        send(new SetVariableMessage(id, value));
    }

    /**
     * Sets if a feature is visible.
     *
     * @param id the feature id.
     * @param visible if the feature is visible.
     */
    public void setFeatureVisible(int id, boolean visible) {
        appearance.setVisible(id, visible);
    }

    /**
     * Sets if the appearance was updated.
     *
     * @param bool the updated flag.
     */
    public void setAppearanceUpdated(boolean bool) {
        this.appearanceUpdated = bool;
    }

    /**
     * Gets if the appearance was updated.
     *
     * @return if the appearance was updated.
     */
    public boolean getAppearanceUpdated() {
        return appearanceUpdated;
    }

    /**
     * Gets the appearance.
     *
     * @return the appearance.
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Adds an item to an inventory.
     *
     * @param id the inventory id.
     * @param item the item to add.
     * @param stack if the item should be stacked in the inventory.
     */
    public void addItem(int id, Item item, boolean stack) {
        Inventory inventory = getInventory(id);
        inventory.add(item, stack);
    }

    /**
     * Gets if an inventory will accept an item.
     *
     * @param id the inventory id.
     * @param item the item to test.
     * @param stack if the item should be stacked in the inventory.
     * @return if the inventory has room for the specified item under the specified stack condition.
     */
    public boolean acceptItem(int id, Item item,  boolean stack) {
        Inventory inventory = inventories.get(id);
        return inventory.accept(item, stack);
    }

    /**
     * Adds an item to an inventory.
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
     * Moves an item from one inventory to another.
     *
     * @param srcId the source inventory id.
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
     * Switches items in an inventory.
     *
     * @param id the inventory id.
     * @param firstSlot the first slot.
     * @param secondSlot the second slot.
     * @param mode the switch mode.
     *
     * @see Inventory#SWAP
     * @see Inventory#INSERT
     */
    public void switchItems(int id, int firstSlot, int secondSlot, int mode) {
        if(!hasInventory(id)) {
            return;
        }
        Inventory inventory = inventories.get(id);
        inventory.switchItems(firstSlot, secondSlot, mode);
    }

    /**
     * Swaps items between inventories.
     *
     * @param srcId the source inventory id.
     * @param destId the destination inventory id.
     * @param firstSlot the first slot.
     * @param secondSlot the second slot.
     * @param stack if the item being shifted from the source inventory should stack, if this is the case
     *              and the items being swapped are the same id the two stacks are combined in the destination
     *              inventory at the specified second slot.
     * @param shift if the items should be shifted in the source inventory from which the item was removed from.
     */
    public void swapItems(int srcId, int destId, int firstSlot, int secondSlot, boolean stack, boolean shift) {
        if(!hasInventory(srcId)) {
            return;
        }

        Inventory src = inventories.get(srcId);
        Inventory dest = getInventory(destId);

        if(firstSlot < 0 || secondSlot < 0 || firstSlot >= src.capacity() || secondSlot >= dest.capacity()) {
            throw new ArrayIndexOutOfBoundsException("Invalid slots");
        }

        Item item = src.remove(firstSlot, shift);
        Item replace = dest.get(secondSlot);

        if(replace != null && replace.getId() == item.getId() && stack) {
            int amount = item.getAmount();
            if(item.getAmount() + replace.getAmount() < 0) {
                amount = Integer.MAX_VALUE - replace.getAmount();
                src.set(firstSlot, new Item(item.getId(), item.getAmount() - amount));
            } else {
                amount += replace.getAmount();
            }
            dest.set(secondSlot, new Item(item.getId(), amount));
        } else {
            src.set(firstSlot, replace);
            dest.set(secondSlot, item);
        }
    }

    /**
     * Gets an item in an inventory.
     *
     * @param id the inventory id.
     * @param slot the slot.
     * @return the item.
     */
    public Item getItem(int id, int slot) {
        if(!hasInventory(id)) {
            return null;
        }
        Inventory inventory = inventories.get(id);
        return inventory.get(slot);
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
        Inventory inventory = inventories.get(id);
        inventory.remove(itemId, amount, shift);
    }

    /**
     * Gets the space for an item in an inventory.
     *
     * @param id the inventory id.
     * @param itemId the item id.
     * @param amount the amount of the item.
     * @param stackable flag for if the item is stackable.
     * @return the amount of the item that the inventory can accept, up to the specified amount.
     */
    public int getInventorySpace(int id, int itemId, int amount, boolean stackable) {
        Inventory inventory = getInventory(id);
        return inventory.getSpaceFor(itemId, amount, stackable);
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
        inventory.clearUpdatedSlots();
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
        InventoryConfig config = InventoryConfig.forId(id);
        Inventory inventory = inventories.getOrDefault(id, new Inventory(config.getSize()));
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
     * Opens a screen.
     *
     * @param interfaceId the interface id.
     */
    public void openScreen(int interfaceId) {
        interfaces.setScreen(interfaceId);
        send(new OpenScreenMessage(interfaceId));
    }

    /**
     * Opens an interface.
     *
     * @param parentId the parent interface id.
     * @param componentId the component id.
     * @param interfaceId the id of the interface to open.
     * @param type the .
     */
    public void openInterface(int parentId, int componentId, int interfaceId, int type) {
        interfaces.openInterface(parentId, componentId, interfaceId, type);
        InterfaceConfig config = InterfaceConfig.forId(interfaceId);
        if(config != null) {
            for(InterfaceComponent component : config.getComponents()) {
                ChildActions actions = component.getChildActions();
                if (actions != null) {
                    send(new AccessOptionMessage(interfaceId, component.getId(), actions.getStart(), actions.getEnd(), actions.getFlags()));
                }
            }
        }
        send(new SetInterfaceMessage(parentId, componentId, interfaceId, type));
    }

    /**
     * Sets the text for an interface.
     *
     * @param widgetId the widget id.
     * @param componentId the component id.
     * @param text the text.
     */
    public void setInterfaceText(int widgetId, int componentId, String text) {
        send(new SetInterfaceTextMessage(widgetId, componentId, text));
    }

    /**
     * Sets if an interface is hidden.
     *
     * @param interfaceId the interface id.
     * @param componentId the component id.
     * @param hidden if the interface is hidden.
     */
    public void setInterfaceHidden(int interfaceId, int componentId, boolean hidden) {
        send(new SetInterfaceHiddenMessage(interfaceId, componentId, hidden));
    }

    /**
     * Gets if an interface is open.
     *
     * @param interfaceId the interface id.
     * @param componentId the component id.
     * @return if the interface is open.
     */
    public boolean isInterfaceOpen(int interfaceId, int componentId) {
        return interfaces.isOpened(interfaceId, componentId);
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
     * @param players the player list.
     */
    public void updateLocalPlayers(EntityList<Player> players) {
        scene.updateLocalPlayers(players, getPosition());
        send(PlayerSynchronizationMessage.create(this));
    }

    /**
     * Updates the local NPC graph.
     *
     * @param npcs the npc list.
     */
    public void updateLocalNpcs(EntityList<NPC> npcs) {
        scene.updateLocalNpcs(npcs, getPosition());
        send(NpcSynchronizationMessage.create(this));
    }

    /**
     * Updates the blocks.
     *
     * @param regions the regions.
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
     * Refreshes the variables.
     */
    public void refreshVariables() {
        VariableSet variables = getVariables();
        for(int id : variables.getUpdated()) {
            send(new SetVariableMessage(id, variables.getValue(id)));
        }
    }

    /**
     * Plays a song.
     *
     * @param id the song id.
     */
    public void playSong(int id) {
        send(new PlaySongMessage(id));
    }


    /**
     * Gets a stat.
     *
     * @param skillId the skill id.
     * @return the stat.
     */
    public int getStat(int skillId) {
        return skills.getStat(skillId);
    }

    /**
     * Gets a level.
     *
     * @param skillId the skill id.
     * @return the level.
     */
    public int getLevel(int skillId) {
        return skills.getLevel(skillId);
    }

    /**
     * Adds experience.
     *
     * @param skill the skill id.
     * @param amount the amount of experience to add.
     */
    public void addExperience(int skill, double amount) {
        skills.giveExperience(skill, amount);
        Double experience = Double.valueOf(skills.getExperience(skill));
        send(new UpdateSkillMessage(skill, experience.intValue(), skills.getStat(skill)));
    }

    /**
     * Gets the skills.
     *
     * @return the skills.
     */
    public SkillSet getSkills() {
        return skills;
    }

    /**
     * Refreshes the skills.
     */
    public void refreshSkills() {
        for(int i = 0; i < Skills.COUNT; i++) {
            send(new UpdateSkillMessage(i, Double.valueOf(skills.getExperience(i)).intValue(), skills.getStat(i)));
        }
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
     * Prints text to the players console.
     *
     * @param text the text.
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
     * Returns the player to a profile model.
     *
     * @return the profile.
     */
    public Profile toProfile() {
        Profile profile = new Profile();
        profile.setUid(uid);

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

        VariableSet variables = getVariables();
        for(Entry<Integer, Integer> entry : variables.getEntries()) {
            VariableModel variable = new VariableModel();
            variable.setId(entry.getKey());
            variable.setValue(entry.getValue());

            profile.addVariable(variable);
        }

        return profile;
    }
}