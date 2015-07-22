package io.mopar.game.msg;

import io.mopar.core.msg.Message;
import io.mopar.game.model.Inventory;

/**
 * @author Hadyn Fitzgerald
 */
public class RefreshInventoryMessage extends Message {

    private Inventory inventory;
    private int id;
    private int widgetId;
    private int componentId;

    public RefreshInventoryMessage(Inventory inventory, int id, int widgetId, int componentId) {
        this.inventory = inventory;
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.id = id;
    }


    public Inventory getInventory() {
        return inventory;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getId() {
        return id;
    }
}
