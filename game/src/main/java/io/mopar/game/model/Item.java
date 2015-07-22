package io.mopar.game.model;

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

    public Item add(int amt) {
        return new Item(id, amount + amt);
    }

    public Item take(int amt) {
        return new Item(id, amount - amt);
    }
}
