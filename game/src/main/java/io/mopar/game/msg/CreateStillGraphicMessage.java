package io.mopar.game.msg;

import io.mopar.game.model.Graphic;

/**
 * @author Hadyn Fitzgerald
 */
public class CreateStillGraphicMessage extends BlockMessage {

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
    private Graphic graphic;

    /**
     *
     * @param x
     * @param y
     * @param graphic
     */
    public CreateStillGraphicMessage(int x, int y, Graphic graphic) {
        this.x = x;
        this.y = y;
        this.graphic = graphic;
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
    public Graphic getGraphic() {
        return graphic;
    }
}
