package io.mopar.game.model;

import javax.xml.parsers.SAXParser;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hadyn Fitzgerald
 */
public class Inventory {

    public static final int SWAP = 0;
    public static final int INSERT = 1;

    /**
     * The items.
     */
    private Item[] items;

    /**
     * The updated slots.
     */
    private Set<Integer> updatedSlots = new HashSet<>();

    /**
     * Constructs a new {@link Inventory};
     */
    public Inventory() {
        this(10);
    }

    /**
     * Constructs a new {@link Inventory};
     *
     * @param initialCapacity The initial capacity.
     */
    public Inventory(int initialCapacity) {
        items = new Item[initialCapacity];
    }

    /**
     * Adds an item.
     *
     * @param item the item.
     * @param stack flag for if the item should append to the current stack. This will however not add more
     *               than the maximum amount allowed amount {@link Integer#MAX_VALUE} to the stack.
     */
    public void add(Item item, boolean stack) {
        if (stack) {
            int slot = slotOf(item.getId());
            int id = item.getId();
            int amount = item.getAmount();
            if(slot != -1) {
                Item itm = items[slot];
                if(item.getAmount() + itm.getAmount() < 0) {
                    amount = Integer.MAX_VALUE - itm.getAmount();
                } else {
                    amount += itm.getAmount();
                }
            } else {
                slot = freeSlot();
            }
            set(slot, new Item(id, amount));
        } else {
            items[freeSlot()] = item;
        }
    }

    /**
     * Adds an item.
     *
     * @param id the item id.
     * @param amount the amount to add.
     * @param stack flag for if the item should append to the current stack. This will however not add more
     *               than the maximum amount allowed amount {@link Integer#MAX_VALUE} to the stack.
     */
    public void add(int id, int amount, boolean stack) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be less than zero");
        }

        if(amount > 0) {
            if (stack) {
                int slot = slotOf(id);
                if (slot == -1) {
                    slot = freeSlot();
                } else {
                    Item item = items[slot];
                    if (amount + item.getAmount() < 0) {
                        amount = Integer.MAX_VALUE - item.getAmount();
                    } else {
                        amount += item.getAmount();
                    }
                }
                set(slot, new Item(id, amount));
            } else {
                int slot = -1;
                while (amount-- > 0) {
                    set(slot = freeSlot(slot + 1), new Item(id));
                }
            }
        }
    }


    /**
     * Sets a slot.
     *
     * @param slot the slot.
     * @param item the item.
     * @return the replaced item.
     */
    public Item set(int slot, Item item) {
        if(slot < 0) {
            throw new ArrayIndexOutOfBoundsException(slot);
        }

        if(slot >= items.length) {
            items = Arrays.copyOf(items, slot + 1);
        }

        Item i = items[slot];
        items[slot] = item;
        updatedSlots.add(slot);
        return i;
    }

    /**
     * Gets an item.
     *
     * @param slot the slot.
     * @return the item for the slot or {@code null} if the slot is greater than the inventories current capacity.
     */
    public Item get(int slot) {
        if(slot < 0) {
            throw new ArrayIndexOutOfBoundsException(slot);
        }
        if(slot >= items.length) {
            return null;
        }
        return items[slot];
    }

    /**
     *
     * @param slot
     * @return
     */
    public int getId(int slot) {
        Item item = get(slot);
        if(item == null) {
            return -1;
        }
        return item.getId();
    }

    /**
     * Removes an item.
     *
     * @param slot the item slot.
     * @param shift if the items should be shifted to the removed slot.
     * @return the removed item.
     */
    public Item remove(int slot, boolean shift) {
        if(slot < 0) {
            throw new ArrayIndexOutOfBoundsException(slot);
        }
        Item item = items[slot];
        items[slot] = null;
        updatedSlots.add(slot);

        if(shift && items.length > slot + 1) {
            System.arraycopy(items, slot + 1, items, slot, items.length - slot - 1);
        }
        return item;
    }

    /**
     * Removes an item from the inventory.
     *
     * @param itemId the item id.
     * @param amount the amount.
     * @param shift if the items should be shifted to the removed item slot.
     * @return the amount of the item that was removed.
     */
    public int remove(int itemId, int amount, boolean shift) {
        int slot = -1;
        while((slot = slotOf(itemId, slot + 1)) != -1) {
            Item item = get(slot);
            int i = amount;
            if(i > item.getAmount()) {
                i = item.getAmount();
                remove(slot, shift);
            } else {
                set(slot, item.take(i));
            }
            amount -= i;
            if(amount < 1) {
                break;
            }
        }
        return amount;
    }

    /**
     *
     * @param slot
     * @return
     */
    public boolean empty(int slot) {
        return get(slot) == null;
    }

    /**
     * Gets the amount of space the inventory has for an item. If the item is marked as stackable,
     * and a stack of the item exists returns the minimum of either {@link Integer#MAX_VALUE} minus
     * the stack amount or the specified amount. If the item is not marked as stackable gets the minimum value
     * of either the empty slots or specified amount.
     *
     * @param itemId the item id.
     * @param amount the amount.
     * @param limit the inventory size limit.
     * @param stackable the stackable flag.
     * @return the amount of items that the inventory can accept.
     */
    public int getSpace(int itemId, int amount, int limit, boolean stackable) {
        if(stackable) {
            int slot = slotOf(itemId);
            if(slot != -1) {
                Item item = items[slot];
                if(item.getAmount() + amount < 0) {
                    amount = Integer.MAX_VALUE - item.getAmount();
                }
            }
        } else {
            int free = emptySlots(limit);
            if(amount > free) {
                amount = free;
            }
        }
        return amount;
    }

    /**
     *
     * @param first
     * @param second
     * @param mode
     */
    public void swap(int first, int second, int mode) {
        if(mode == SWAP) {
            Item item = get(second);
            set(second, get(first));
            set(first, item);
        } else {
            // TODO
        }
    }

    /**
     *
     * @param item
     * @param size
     * @param stack
     * @return
     */
    public boolean accept(Item item, int size, boolean stack) {
        if(stack && slotOf(item.getId()) != -1) {
            return true;
        }
        return freeSlot() < size;
    }

    /**
     * Gets the next slot of the specified item.
     *
     * @param itemId the item id.
     * @return the next slot from the inventory start, inclusive. If the item does not
     *          exist from the inventory start to the current inventory capacity exclusive then {@code -1} is returned.
     */
    public int slotOf(int itemId) {
        return slotOf(itemId, 0);
    }

    /**
     * Gets the next slot of the specified item.
     *
     * @param itemId the item id.
     * @param start the start slot.
     * @return the next slot from the specified start value, inclusive. If the item does not
     *          exist from the start to the current inventory capacity exclusive then {@code -1} is returned.
     */
    public int slotOf(int itemId, int start) {
        for(int i = start; i < items.length; i++) {
            if(items[i] != null && items[i].getId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the amount of empty slots.
     *
     * @param limit the size limit of the inventory.
     * @return the amount of empty slots.
     */
    public int emptySlots(int limit) {
        int count = limit - items.length;
        int i;
        for(i = 0; i < items.length; i++) {
            if(items[i] != null) {
                continue;
            }
            count++;
        }
        return count;
    }

    /**
     * Gets the next free slot from the start of the inventory.
     *
     * @return the free slot from the start of the inventory, inclusive. If the capacity is reached
     *          then the current capacity is returned.
     */
    public int freeSlot() {
        return freeSlot(0);
    }

    /**
     * Gets the next free slot from the specified start slot, inclusive.
     *
     * @param start the start slot.
     * @return the free slot from the start, inclusive. If the capacity is reached then the current capacity is returned.
     */
    public int freeSlot(int start) {
        for(int i = start; i < items.length; i++) {
            if(items[i] == null) {
                return i;
            }
        }
        return items.length;
    }

    /**
     *
     * @param itemId
     * @return
     */
    private boolean contains(int itemId) {
        return contains(itemId, 1);
    }

    /**
     * Gets if the inventory contains a specified amount of an item.
     *
     * @param itemId the item id.
     * @param amount the amount of the item.
     * @return if the inventory contains the amount of the item.
     */
    public boolean contains(int itemId, int amount) {
        int count = 0;
        for(int i = 0; i < items.length; i++) {
            Item item = items[i];
            if(item == null) {
                continue;
            }

            if(item.getId() == itemId) {
                count += item.getAmount();
                if(count >= amount) {
                    break;
                }
            }
        }
        return count >= amount;
    }

    /**
     * Gets the count of an item in the inventory.
     *
     * @param itemId the item id.
     * @return the amount of the item in the inventory.
     */
    public int count(int itemId) {
        int count = 0;
        for(int i = 0; i < items.length; i++) {
            Item item = items[i];
            if(item == null) {
                continue;
            }

            if(item.getId() == itemId) {
                count += item.getAmount();
            }
        }
        return count;
    }

    /**
     * Gets the slots that have been updated for this inventory.
     *
     * @return the updated slots.
     */
    public Set<Integer> getUpdatedSlots() {
        return updatedSlots;
    }

    /**
     * Resets the updated slots.
     */
    public void resetUpdatedSlots() {
        updatedSlots.clear();
    }

    /**
     * Gets the current capacity of the inventory.
     *
     * @return the capacity.
     */
    public int capacity() {
        return items.length;
    }

    /**
     * Clears the inventory.
     */
    public void clear() {
        for(int i = 0; i < items.length; i++) {
            items[i] = null;
        }
    }

}
