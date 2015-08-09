package io.mopar.game.req;

import io.mopar.core.Request;
import io.mopar.game.model.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class RoutePlayerRequest extends Request {

    /**
     * The player id.
     */
    private int playerId;

    /**
     * The waypoints.
     */
    private List<Waypoint> waypoints = new ArrayList<>();

    /**
     * Constructs a new {@link RoutePlayerRequest};
     *
     * @param playerId The player id.
     */
    public RoutePlayerRequest(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player id.
     *
     * @return the player id.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     *
     * @param waypoint
     */
    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
    }

    /**
     *
     * @return
     */
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }
}
