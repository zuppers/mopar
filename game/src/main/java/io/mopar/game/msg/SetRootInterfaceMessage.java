package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetRootInterfaceMessage extends Message {

    /**
     * The widget id.
     */
    private int widgetId;

    /**
     * Constructs a new {@link SetRootInterfaceMessage};
     *
     * @param widgetId The widget id.
     */
    public SetRootInterfaceMessage(int widgetId) {
        this.widgetId = widgetId;
    }

    /**
     * Gets the widget id.
     *
     * @return The widget id.
     */
    public int getWidgetId() {
        return widgetId;
    }
}