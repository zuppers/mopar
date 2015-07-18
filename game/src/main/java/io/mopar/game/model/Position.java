package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class Position {

    /**
     * The x coordinate.
     */
    private int x;

    /**
     * The y coordinate.
     */
    private int y;

    /**
     * The plane.
     */
    private int plane;

    /**
     * Constructs a new {@link Position};
     */
    public Position() {}

    /**
     * Constructs a new {@link Position};
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param x
     * @param y
     * @param plane
     */
    public Position(int x, int y, int plane) {
        this.x = x;
        this.y = y;
        this.plane = plane;
    }

    /**
     * Gets the x coordinate.
     *
     * @return The x value.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return The y value.
     */
    public int getY() {
        return y;
    }


    /**
     * Gets the plane or z coordinate.
     *
     * @return The plane.
     */
    public int getPlane() {
        return plane;
    }

    /**
     * Gets the block x coordinate.
     *
     * @return The block x.
     */
    public int getBlockX() {
        return x >> 3;
    }

    /**
     * Gets the block y coordinate.
     *
     * @return The block y.
     */
    public int getBlockY() {
        return y >> 3;
    }

    /**
     * Gets the encoded block hash from the x, y coordinates, and plane.
     *
     * @return The block code.
     */
    public int getBlockHash() { return plane << 30 | getBlockX() << 16 | getBlockY(); }

    /**
     * Gets the local x coordinate.
     *
     * @return the scene x coordinate.
     */
    public int getLocalX()  {
        return x - ((getBlockX() - (Scene.LENGTH >> 4)) << 3);
    }

    /**
     * Gets the local y coordinate.
     *
     * @return the scene y coordinate.
     */
    public int getLocalY()  {
        return y - ((getBlockY() - (Scene.LENGTH >> 4)) << 3);
    }

    /**
     * Gets the local x coordinate.
     *
     * @param position The relative position.
     * @return the scene x coordinate.
     */
    public int getLocalX(Position position) {
        return x - ((position.getBlockX() - (Scene.LENGTH >> 4)) << 3);
    }

    /**
     * Gets the local y coordinate.
     *
     * @param position The relative position.
     * @return The local block y.
     */
    public int getLocalY(Position position) {
        return y - ((position.getBlockY() - (Scene.LENGTH >> 4)) << 3);
    }

    /**
     * Gets the region x coordinate.
     *
     * @return the region x.
     */
    public int getRegionX() {
        return x >> 6;
    }

    /**
     * Gets the region y coordinate.
     *
     * @return the region y.
     */
    public int getRegionY() {
        return x >> 6;
    }


    /**
     * Offsets the position.
     *
     * @param vector The vector.
     * @return The offset position.
     */
    public Position offset(Vector vector) {
        return offset(vector.dx(), vector.dy());
    }

    /**
     * Offsets the position.
     *
     * @param dx The delta x value.
     * @param dy The delta y value.
     * @return The offset position.
     */
    public Position offset(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    /**
     * Gets if the position is within a certain amount of rectangular distance to this position.
     *
     * @param position The position.
     * @param distance The distance.
     * @return If the position is within the specified distance.
     */
    public boolean within(Position position, int distance) {
        int dx = position.x - x;
        int dy = position.y - y;
        return dx <= distance - 1 && dx >= -distance && dy <= distance - 1 && dy >= -distance;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(!(obj instanceof Position)) {
            return false;
        }

        Position compare = (Position) obj;
        return compare.x == x && compare.y == y && compare.plane == plane;
    }
}