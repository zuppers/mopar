package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class OpenScreenMessage extends Message {

    /**
     * The widget id.
     */
    private int widgetId;

    /**
     * Constructs a new {@link OpenScreenMessage};
     *
     * @param widgetId The widget id.
     */
    public OpenScreenMessage(int widgetId) {
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