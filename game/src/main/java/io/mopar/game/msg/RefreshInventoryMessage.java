package io.mopar.game.msg;

import io.mopar.core.msg.Message;
import io.mopar.game.model.Inventory;

/**
 * @author Hadyn Fitzgerald
 */
public class RefreshInventoryMessage extends Message {

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
     * @param inventory
     * @param id
     * @param widgetId
     * @param componentId
     */
    public RefreshInventoryMessage(Inventory inventory, int id, int widgetId, int componentId) {
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
}