package io.mopar.game.msg;

/**
 * @author Hadyn Fitzgerald
 */
public class UpdateGameObjectMessage extends BlockMessage {

    /**
     *
     */
    private int x;

    /**
     *
     */
    private int y;

    /**
     *
     */
    private int type;

    /**
     *
     */
    private int configId;

    /**
     *
     */
    private int orientation;

    /**
     *
     * @param x
     * @param y
     * @param type
     * @param configId
     * @param orientation
     */
    public UpdateGameObjectMessage(int x, int y, int type, int configId, int orientation) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.configId = configId;
        this.orientation = orientation;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public int getConfigId() {
        return configId;
    }

    /**
     *
     * @return
     */
    public int getOrientation() {
        return orientation;
    }
}
