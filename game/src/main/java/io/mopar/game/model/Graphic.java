package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class Graphic {

    /**
     *
     */
    private int type;

    /**
     *
     */
    private int height;

    /**
     *
     */
    private int delay;

    /**
     *
     * @param type
     * @param height
     * @param delay
     */
    public Graphic(int type, int height, int delay) {
        this.type = type;
        this.height = height;
        this.delay = delay;
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
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return
     */
    public int getDelay() {
        return delay;
    }
}
