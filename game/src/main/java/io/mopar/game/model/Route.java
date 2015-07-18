package io.mopar.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class Route {

    public static final class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }

        public List<Step> interpolate(Position position) {
            int dx = x - position.getX();
            int dy = y - position.getY();

            if(dx == 0 && dy == 0) {
                return Collections.emptyList();
            }

            int max = Math.max(Math.abs(dx), Math.abs(dy));

            List<Step> steps = new ArrayList<>();
            for(int i = 0; i < max; i++) {

                steps.add(Step.forVector(dx, dy));

                if(dx > 0) {
                    dx--;
                } else if(dx < 0) {
                    dx++;
                }

                if(dy > 0) {
                    dy--;
                } else if(dy < 0) {
                    dy++;
                }
            }

            return steps;
        }
    }

    /**
     * The points.
     */
    private List<Point> points = new ArrayList<>();

    /**
     * Constructs a new {@link Route};
     */
    public Route() {}

    /**
     * Appends a point for the route.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void appendPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    /**
     * Gets the points.
     *
     * @return the points.
     */
    public List<Point> getPoints() {
        return points;
    }
}
