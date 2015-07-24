package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * Created by hadyn on 7/23/2015.
 */
public class AccessOptionsMessage extends Message {
    private int widgetId;
    private int componentId;
    private int start;
    private int end;
    private int flags;

    public AccessOptionsMessage(int widgetId, int componentId, int start, int end, int flags) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.start = start;
        this.end = end;
        this.flags = flags;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getFlags() {
        return flags;
    }
}
