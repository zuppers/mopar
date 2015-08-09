package io.mopar.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public final class Waypoint {

    /**
     *
     */
    private int x;

    /**
     *
     */
    private int y;

    /**
     *
     * @param x
     * @param y
     */
    public Waypoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }


    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param position
     * @return
     */
    public List<Step> interpolate(Position position) {
        int dx = x - position.getX();
        int dy = y - position.getY();

        if(dx == 0 && dy == 0) {
            return Collections.emptyList();
        }

        int max = Math.max(Math.abs(dx), Math.abs(dy));

        List<Step> steps = new ArrayList<>();
        for(int i = 0; i < max; i++) {

            steps.add(new Step(Direction.forVector(dx, dy)));

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
