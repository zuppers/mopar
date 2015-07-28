package io.mopar.account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadyn on 7/27/2015.
 */
public class InventoryModel {

    /**
     * The id.
     */
    private int id;

    /**
     * The items.
     */
    private List<ItemModel> items = new ArrayList<>();

    /**
     * Constructs a new {@link InventoryModel};
     */
    public InventoryModel() {}

    /**
     * Gets the items.
     *
     * @return the items.
     */
    public List<ItemModel> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addItem(ItemModel item) {
        items.add(item);
    }
}
