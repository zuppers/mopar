package io.mopar.game.msg;

/**
 * @author Hadyn Fitzgerald
 */
public class ResetBlockMessage extends BlockMessage {

    /**
     * The x coordinate.
     */
    private int x;

    /**
     * The y coordinate.
     */
    private int y;

    /**
     * Constructs a new {@link ResetBlockMessage};
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public ResetBlockMessage(int x, int y) {
        this.x = x;
        this.y = y;
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
    public int getX() {
        return x;
    }
}
