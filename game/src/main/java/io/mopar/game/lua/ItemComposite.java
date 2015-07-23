package io.mopar.game.lua;

import io.mopar.core.lua.Composite;
import io.mopar.game.model.Item;

/**
 * @author Hadyn Fitzgerald
 */
public class ItemComposite implements Composite {

    private Item item;

    public ItemComposite(Item item) {
        this.item = item;
    }

    public int id() {
        return item.getId();
    }

    public int amount() {
        return item.getAmount();
    }

    public Item getItem() {
        return item;
    }
}
