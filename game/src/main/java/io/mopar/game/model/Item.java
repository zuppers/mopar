package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class Item {
    private int id;
    private int amount;

    public Item(int id, int i) {
        this.id = id;
        this.amount = i;
    }

    public Item(int id) {
        this(id, 1);
    }

    public int getId() {
        return id;
    }

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
