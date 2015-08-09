package io.mopar.rs2.msg.game;

import io.mopar.core.msg.Message;
import io.mopar.game.model.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class RouteMessage extends Message {

    /**
     * The points.
     */
    private List<Waypoint> waypoints = new ArrayList<>();

    /**
     * Constructs a new {@link RouteMessage};
     */
    public RouteMessage() {}

    /**
     * Appends a point for the route.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void appendPoint(int x, int y) {
        waypoints.add(new Waypoint(x, y));
    }

    /**
     *
     * @return
     */
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }
}
