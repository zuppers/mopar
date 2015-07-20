package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class ButtonActionRequest extends Request {

    private int playerId;
    private int widgetId;
    private int componentId;
    private int childId;
    private int option;

    public ButtonActionRequest(int playerId, int widgetId, int componentId, int childId, int option) {
        this.playerId = playerId;
        this.widgetId = widgetId;
        this.childId = childId;
        this.componentId = componentId;
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

    public int getChildId() {
        return childId;
    }

    public int getOption() {
        return option;
    }
}
