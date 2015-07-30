package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class Step {

    /**
     * The direction to step in.
     */
    private Direction direction;

    /**
     * The time the step was taken.
     */
    private int time = -1;

    /**
     *
     * @param direction
     */
    public Step(Direction direction) {
        this.direction = direction;
    }
    /**
     *
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     *
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     *
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     *
     * @return
     */
    public int toInteger() {
        return direction.toInteger();
    }

    /**
     *
     * @return
     */
    public Vector asVector() {
        return direction.asVector();
    }
}
