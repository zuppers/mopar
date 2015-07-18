package io.mopar.game.res;

import io.mopar.core.Response;
import io.mopar.game.req.GetWorldTimeRequest;

/**
 * Created by hadyn on 6/20/15.
 */
public class GetWorldTimeResponse extends Response {

    /**
     * The current world time.
     */
    private int time;

    /**
     * Constructs a new {@link GetWorldTimeRequest};
     *
     * @param time The current world time.
     */
    public GetWorldTimeResponse(int time) {
        this.time = time;
    }

    /**
     * Gets the time.
     *
     * @return The time.
     */
    public int getTime() {
        return time;
    }
}
