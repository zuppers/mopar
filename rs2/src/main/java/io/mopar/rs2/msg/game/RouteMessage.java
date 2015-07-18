package io.mopar.rs2.msg.game;

import io.mopar.core.msg.Message;
import io.mopar.game.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class RouteMessage extends Message {

    /**
     * The points.
     */
    private Route route = new Route();

    /**
     * Constructs a new {@link RouteMessage};
     */
    public RouteMessage() {}

    public Route getRoute() {
        return route;
    }

    /**
     * Appends a point for the route.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void appendPoint(int x, int y) {
        route.appendPoint(x, y);
    }
}
