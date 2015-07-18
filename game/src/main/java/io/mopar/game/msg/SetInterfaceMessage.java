package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetInterfaceMessage extends Message {
    private int targetId;
    private int childId;
    private int widgetId;
    private int type;

    public SetInterfaceMessage(int targetId, int childId, int widgetId, int type) {
        this.targetId = targetId;
        this.childId = childId;
        this.widgetId = widgetId;
        this.type = type;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getComponentId() {
        return childId;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getType() {
        return type;
    }
}
