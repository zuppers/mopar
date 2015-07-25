package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class AccessOptionMessage extends Message {
    private int widgetId;
    private int componentId;
    private int start;
    private int end;
    private int flags;

    public AccessOptionMessage(int widgetId, int componentId, int start, int end, int flags) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.start = start;
        this.end = end;
        this.flags = flags;
    }

    /**
     * Gets the widget id.
     *
     * @return the widget id.
     */
    public int getWidgetId() {
        return widgetId;
    }

    /**
     * Gets the component id.
     *
     * @return the component id.
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * Gets the start child id.
     *
     * @return the start child id.
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the end child id, inclusive.
     *
     * @return the end child id.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Gets the access flags.
     *
     * @return the flags.
     */
    public int getFlags() {
        return flags;
    }
}
