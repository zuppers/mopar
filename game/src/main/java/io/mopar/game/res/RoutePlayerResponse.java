package io.mopar.game.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class RoutePlayerResponse extends Response {
    public static final int OK = 0;
    public static final int PLAYER_DOES_NOT_EXIST = 1;

    private int status;

    public RoutePlayerResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
