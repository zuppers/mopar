package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * Created by hadyn on 7/22/2015.
 */
public class ItemMenuActionRequest extends Request {
    private int playerId;
    private int widgetId;
    private int componentId;
    private int itemId;
    private int slot;
    private int option;

    public ItemMenuActionRequest(int playerId, int widgetId, int componentId, int itemId, int slot, int option) {
        this.playerId = playerId;
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.itemId = itemId;
        this.slot = slot;
        this.option = option;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getSlot() {
        return slot;
    }

    public int getOption() {
        return option;
    }
}
