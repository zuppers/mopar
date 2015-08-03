package io.mopar.game.msg;

/**
 * @author Hadyn Fitzgerald
 */
public class RemoveGameObjectMessage extends BlockMessage {

    private int x;
    private int y;
    private int type;
    private int orientation;

    public RemoveGameObjectMessage(int x, int y, int type, int orientation) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.orientation = orientation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public int getOrientation() {
        return orientation;
    }
}
