package io.mopar.rs2.msg.game;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SwapItemMessage extends Message {
    private int widgetId;
    private int componentId;
    private int firstSlot;
    private int secondSlot;
    private int mode;

    public SwapItemMessage(int widgetId, int componentId, int firstSlot, int secondSlot, int mode) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.firstSlot = firstSlot;
        this.secondSlot = secondSlot;
        this.mode = mode;
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
