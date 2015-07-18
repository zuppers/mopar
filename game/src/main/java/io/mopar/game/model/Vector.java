package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class Vector {

    /**
     * The delta x.
     */
    private int dx;

    /**
     * The delta y.
     */
    private int dy;

    /**
     * The delta z.
     */
    private int dz;

    /**
     * Constructs a new {@link Vector};
     *
     * @param dx The delta x value.
     * @param dy The delta y value.
     */
    public Vector(int dx, int dy) {
        this(dx, dy, 0);
    }

    /**
     * Constructs a new {@link Vector};
     *
     * @param dx The delta x value.
     * @param dy The delta y value.
     * @param dz The delta z value.
     */
    public Vector(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    /**
     * Get the delta x.
     *
     * @return The delta x.
     */
    public int dx() {
        return dx;
    }

    /**
     * Get the delta y.
     *
     * @return The delta y.
     */
    public int dy() {
        return dy;
    }

    /**
     * Adds two vectors.
     *
     * @param vector The vector.
     * @return The result vector.
     */
    public Vector add(Vector vector) {
        return new Vector(dx + vector.dx, dy + vector.dy, dz + vector.dz);
    }

    /**
     * Normalizes a vector.
     *
     * @return The normalized vector or unit vector.
     */
    public Vector normalize() {
        return new Vector(dx / Math.abs(dx), dy / Math.abs(dy), dz / Math.abs(dz));
    }
}