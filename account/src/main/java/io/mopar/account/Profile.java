package io.mopar.account;

/**
 * @author Hadyn Fitzgerald
 */
public class Profile {

    /**
     * The user id.
     */
    private long uid;

    /**
     * The x coordinate.
     */
    private int x;

    /**
     * The y coordinate.
     */
    private int y;

    /**
     * The plane coordinate.
     */
    private int plane;

    public Profile(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPlane(int plane) {
        this.plane = plane;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getPlane() {
        return plane;
    }
}
