package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * Created by hadyn on 7/24/2015.
 */
public class SetInterfaceTextMessage extends Message {
    private int widgetId;
    private int componentId;
    private String text;

    public SetInterfaceTextMessage(int widgetId, int componentId, String text) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.text = text;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getComponentId() {
        return componentId;
    }

    public String getText() {
        return text;
    }
}
