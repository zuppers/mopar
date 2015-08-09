package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class SetInterfaceMessage extends Message {

    /**
     *
     */
    private int targetId;

    /**
     *
     */
    private int childId;

    /**
     *
     */
    private int widgetId;

    /**
     *
     */
    private int type;

    /**
     *
     * @param targetId
     * @param childId
     * @param widgetId
     * @param type
     */
    public SetInterfaceMessage(int targetId, int childId, int widgetId, int type) {
        this.targetId = targetId;
        this.childId = childId;
        this.widgetId = widgetId;
        this.type = type;
        System.out.println(type);
    }

    /**
     *
     * @return
     */
    public int getTargetId() {
        return targetId;
    }

    /**
     *
     * @return
     */
    public int getComponentId() {
        return childId;
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
    public int getType() {
        return type;
    }
}
