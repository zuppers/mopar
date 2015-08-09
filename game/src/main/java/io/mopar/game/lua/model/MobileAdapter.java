package io.mopar.game.lua.model;

import io.mopar.game.model.Mobile;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class MobileAdapter extends EntityAdapter {

    /**
     * The mobile.
     */
    private Mobile mobile;

    /**
     * Constructs a new {@link MobileAdapter};
     *
     * @param mobile the mobile.
     */
    protected MobileAdapter(Mobile mobile) {
        super(mobile);
        this.mobile = mobile;
    }

    /**
     * Sets if the mobile is running.
     *
     * @param running the running flag.
     */
    public void SetRunning(boolean running) {
        mobile.setRunning(running);
    }

    /**
     * Gets if the mobile is running.
     *
     * @return the running flag.
     */
    public boolean GetRunning() {
        return mobile.isRunning();
    }

    /**
     * Appends a waypoint for the mobile to walk to a specific point. The mobile will not immediately
     * walk to this point if there pending waypoints to be processed before the given point.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void WalkTo(int x, int y) {
        mobile.addWaypoint(x, y);
    }

    /**
     * Sets if the mobiles movement is clipped.
     *
     * @param clipped the clipped flag.
     */
    public void SetClipped(boolean clipped) {
        mobile.setClipped(clipped);
    }

    /**
     * Gets if the mobiles movement is clipped.
     *
     * @return the clipped flag.
     */
    public boolean GetClipped() {
        return mobile.isClipped();
    }
}
