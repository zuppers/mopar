package io.mopar.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class BrensenhamLineAlgorithm {

    /**
     *
     * @param plane
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    public static List<Position> getPoints(int plane, int x0, int y0, int x1, int y1) {
        return getPoints(new Position(x0, y0, plane), new Position(x1, y1, plane));
    }

    /**
     *
     * @return
     */
    public static List<Position> getPoints(Position src, Position dest) {
        if(src.getPlane() != dest.getPlane()) {
            throw new IllegalArgumentException("Expected source and destination points to be on the same plane");
        }

        List<Position> points = new ArrayList<>();

        // Calculate the delta x and y values
        int dx = Math.abs(dest.getX() - src.getX());
        int dy = Math.abs(dest.getY() - src.getY());

        // Get the step values
        int stepX = src.getX() < dest.getX() ? 1 : -1;
        int stepY = src.getY() < dest.getY() ? 1 : -1;

        // Calculate the error
        int err = dx - dy;
        int e2;

        while(true) {
            points.add(src);

            if(src.equals(dest)) {
                break;
            }

            e2 = err * 2;

            if (e2 > -dy) {
                err = err - dy;
                src = src.offset(stepX, 0);
            }

            if (e2 < dy) {
                err = err - dy;
                src = src.offset(0, stepY);
            }
        }

        return points;
    }

    /**
     * Prevent instantiation.
     */
    private BrensenhamLineAlgorithm() {}
}
