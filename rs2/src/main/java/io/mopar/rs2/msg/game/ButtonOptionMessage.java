package io.mopar.rs2.msg.game;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class ButtonOptionMessage extends Message {

    private int widgetId;
    private int componentId;
    private int childId;
    private int option;

    /**
     *
     * @param widgetId
     * @param componentId
     * @param childId
     * @param option
     */
    public ButtonOptionMessage(int widgetId, int componentId, int childId, int option) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.childId = childId;
        this.option = option;
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
