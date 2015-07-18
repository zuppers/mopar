package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public enum Step {

    /**
     * North step.
     */
    NORTH(0, 1),

    /**
     * North east step.
     */
    NORTH_EAST(1, 1),

    /**
     * East step.
     */
    EAST(1, 0),

    /**
     * South east step.
     */
    SOUTH_EAST(1, -1),

    /**
     * South step.
     */
    SOUTH(0, -1),

    /**
     * South west step.
     */
    SOUTH_WEST(-1, -1),

    /**
     * West step.
     */
    WEST(-1, 0),

    /**
     * North west step.
     */
    NORTH_WEST(-1, 1);

    /**
     * The delta x and y values.
     */
    private int dx, dy;

    /**
     * Constructs a new {@link Step};
     *
     * @param dx The delta x value.
     * @param dy The delta y value.
     */
    Step(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Gets the delta x value.
     *
     * @return the delta x.
     */
    public int getDeltaX() {
        return dx;
    }

    /**
     * Gets the delta y value.
     *
     * @return the delta y.
     */
    public int getDeltaY() {
        return dy;
    }

    /**
     * Gets the step as a vector.
     *
     * @return the vector.
     */
    public Vector asVector() {
        return new Vector(dx, dy);
    }

    /**
     * Gets the step as an integer.
     *
     * @return the integer value.
     */
    public int toInteger() {
        switch (this) {
            case NORTH_WEST:
                return 0;
            case NORTH:
                return 1;
            case NORTH_EAST:
                return 2;
            case WEST:
                return 3;
            case EAST:
                return 4;
            case SOUTH_WEST:
                return 5;
            case SOUTH:
                return 6;
            case SOUTH_EAST:
                return 7;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Gets the step for the provided vector.
     *
     * @param vector The vector.
     * @return the step.
     */
    public static Step forVector(Vector vector) {
        return forVector(vector.dx(), vector.dy());
    }

    /**
     * Gets the step for the provided vector values. Uses the normalized vector magnitudes.
     *
     * @param dx The delta x value.
     * @param dy The delta y value.
     * @return the step.
     */
    public static Step forVector(int dx, int dy) {

        // Normalize
        if(dx != 0) dx = dx / Math.abs(dx);
        if(dy != 0) dy = dy / Math.abs(dy);

        if(dx > 0) {
            if (dy > 0) {
                return Step.NORTH_EAST;
            } else if (dy < 0) {
                return Step.SOUTH_EAST;
            } else {
                return Step.EAST;
            }
        } else if(dx < 0) {
            if(dy > 0) {
                return Step.NORTH_WEST;
            } else if(dy < 0) {
                return Step.SOUTH_WEST;
            } else {
                return Step.WEST;
            }
        } else {
            if(dy > 0) {
                return Step.NORTH;
            } else {
                return Step.SOUTH;
            }
        }
    }
}