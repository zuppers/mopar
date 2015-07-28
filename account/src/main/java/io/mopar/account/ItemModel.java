package io.mopar.account;

/**
 * @author Hadyn Fitzgerald
 */
public class ItemModel {

    /**
     * The id.
     */
    private int id;

    /**
     * The amount.
     */
    private int amount;

    /**
     * The slot.
     */
    private int slot;

    /**
     * Construct s anew {@link ItemModel};
     */
    public ItemModel() {}

    public int getSlot() {
        return slot;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
