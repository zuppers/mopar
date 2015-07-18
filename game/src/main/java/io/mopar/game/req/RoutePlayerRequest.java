package io.mopar.game.req;

import io.mopar.core.Request;
import io.mopar.game.model.Route;

/**
 * @author Hadyn Fitzgerald
 */
public class RoutePlayerRequest extends Request {

    /**
     * The player id.
     */
    private int playerId;

    /**
     * The route.
     */
    private Route route;


    /**
     * Constructs a new {@link RoutePlayerRequest};
     *
     * @param playerId The player id.
     * @param route The route.
     */
    public RoutePlayerRequest(int playerId, Route route) {
        this.playerId = playerId;
        this.route = route;
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
     * Gets the route.
     *
     * @return the route.
     */
    public Route getRoute() {
        return route;
    }
}
