package io.mopar.game.msg;

import io.mopar.core.msg.Message;
import io.mopar.game.model.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadyn on 7/21/2015.
 */
public class UpdateInventoryMessage extends Message {

    /**
     *
     */
    private Inventory inventory;

    /**
     *
     */
    private int id;

    /**
     *
     */
    private int widgetId;

    /**
     *
     */
    private int componentId;

    /**
     *
     */
    private List<Integer> slots = new ArrayList<>();

    /**
     *
     * @param inventory
     * @param id
     * @param widgetId
     * @param componentId
     */
    public UpdateInventoryMessage(Inventory inventory, int id, int widgetId, int componentId) {
        this.inventory = inventory;
        this.id = id;
        this.widgetId = widgetId;
        this.componentId = componentId;
    }

    /**
     *
     * @return
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getWidgetId() {
        return widgetId;
    }

    /**
     *
     * @return
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     *
     * @param slot
     */
    public void addSlot(int slot) {
        slots.add(slot);
    }

    /**
     *
     * @return
     */
    public List<Integer> getSlots() {
        return slots;
    }
}