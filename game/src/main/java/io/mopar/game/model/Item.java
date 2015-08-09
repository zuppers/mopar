package io.mopar.game.model;

import io.mopar.game.config.ItemConfig;

/**
 * @author Hadyn Fitzgerald
 */
public class Item {

    /**
     * The id.
     */
    private int id;

    /**
     * The amount.
     */
    private int amount;

    /**
     * Constructs a new {@link Item};
     *
     * @param id the id.
     * @param amount the amount.
     */
    public Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * Constructs a new {@link Item};
     *
     * @param id the id.
     */
    public Item(int id) {
        this(id, 1);
    }

    /**
     * Gets the id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the amount.
     *
     * @return the amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the configuration for the item.
     *
     * @return the configuration.
     */
    public ItemConfig getConfig() {
        return ItemConfig.forId(id);
    }

    /**
     *
     * @param i
     * @return
     */
    public Item take(int i) {
        return new Item(id, amount - i);
    }
}
