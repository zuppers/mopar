package io.mopar.game.model;

import java.util.Arrays;

/**
 * @author Hadyn Fitzgerald
 */
public class Inventory {

    /**
     * The items.
     */
    private Item[] items;

    /**
     *
     */
    public Inventory() {
        this(30);
    }

    /**
     *
     */
    public Inventory(int initialCapacity) {
        items = new Item[initialCapacity];
    }

    /**
     *
     * @param id
     * @param amount
     * @param stack
     */
    public void add(int id, int amount, boolean stack) {
        if(amount < 1) {
            throw new IllegalArgumentException("");
        }

        if(stack) {
            int slot = -1;
            while((slot = slotOf(id, slot + 1)) != -1) {
                Item item = items[slot];
                int i = amount;
                if(amount + item.getAmount() < 0) {
                    i = Integer.MAX_VALUE - item.getAmount();
                }
                set(slot, item.add(i));
                amount -= i;
                if(amount < 1) {
                    break;
                }
            }

            if(amount > 0) {
                set(freeSlot(), new Item(id, amount));
            }
        } else {
            int slot = -1;
            while(amount-- > 0) {
                set(slot = freeSlot(slot + 1), new Item(id));
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
        return i;
    }

    /**
     *
     *
     * @param slot
     * @return
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
    public Item remove(int slot, boolean shift) {
        if(slot < 0) {
            throw new ArrayIndexOutOfBoundsException(slot);
        }
        Item item = items[slot];
        items[slot] = null;

        if(shift && items.length > slot + 1) {
            System.arraycopy(items, slot + 1, items, slot, items.length - slot - 1);
        }
        return item;
    }

    /**
     *
     * @param itemId
     * @param amount
     * @return
     */
    public Item remove(int itemId, int amount, boolean shift) {
        int slot = -1;
        while((slot = slotOf(itemId, slot + 1)) != -1) {
            Item item = get(slot);
            int i = amount;
            if(i > item.getAmount()) {
                i = item.getAmount();
                remove(i, shift);
            } else {
                set(slot, item.take(i));
            }
            amount -= i;
            if(amount < 1) {
                break;
            }
        }
        return new Item(itemId, amount);
    }

    /**
     *
     * @param itemId
     * @return
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
     *
     * @return
     */
    public int freeSlot() {
        return freeSlot(0);
    }

    /**
     *
     * @param start
     * @return
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
     * @param amount
     * @return
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
     *
     *
     * @param itemId
     * @return
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
     * Clears the inventory.
     */
    public void clear() {
        for(int i = 0; i < items.length; i++) {
            items[i] = null;
        }
    }
}
