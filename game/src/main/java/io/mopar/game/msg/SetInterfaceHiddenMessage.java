package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetInterfaceHiddenMessage extends Message {

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
    private boolean hidden;

    /**
     *
     * @param widgetId
     * @param componentId
     * @param hidden
     */
    public SetInterfaceHiddenMessage(int widgetId, int componentId, boolean hidden) {
        this.widgetId = widgetId;
        this.componentId = componentId;
        this.hidden = hidden;
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
    public boolean isHidden() {
        return hidden;
    }
}
