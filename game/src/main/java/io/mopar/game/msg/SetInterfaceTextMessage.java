package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetInterfaceTextMessage extends Message {

    /**
     *
     */
    private int widgetId;

    /**
     *
     */
    private int componentId;

    /**
     *
     */
    private String text;

    /**
     *
     * @param widgetId
     * @param componentId
     * @param text
     */
    public SetInterfaceTextMessage(int widgetId, int componentId, String text) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.text = text;
    }

    /**
     *
     * @return
     */
    public int getWidgetId() {
        return widgetId;
    }

    /**
     *
     * @return
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }
}
