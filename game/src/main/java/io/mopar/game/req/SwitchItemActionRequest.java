package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class SwitchItemActionRequest extends Request {

    private int playerId;
    private int widgetId;
    private int componentId;
    private int firstSlot;
    private int secondSlot;
    private int mode;

    public SwitchItemActionRequest(int playerId, int widgetId, int componentId, int firstSlot, int secondSlot, int mode) {
        this.playerId = playerId;
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.firstSlot = firstSlot;
        this.secondSlot = secondSlot;
        this.mode = mode;
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

    public int getFirstSlot() {
        return firstSlot;
    }

    public int getSecondSlot() {
        return secondSlot;
    }

    public int getMode() {
        return mode;
    }
}
