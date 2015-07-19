package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetInterfaceHiddenMessage extends Message {
    private int widgetId;
    private int componentId;
    private boolean hidden;

    public SetInterfaceHiddenMessage(int widgetId, int componentId, boolean hidden) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.hidden = hidden;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getComponentId() {
        return componentId;
    }

    public boolean isHidden() {
        return hidden;
    }
}
