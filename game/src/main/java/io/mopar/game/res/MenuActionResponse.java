package io.mopar.game.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class MenuActionResponse extends Response {

    /**
     * Status for a successful request.
     */
    public static final int OK = 0;

    /**
     * Status for when the specified source in the request does not exist.
     */
    public static final int SOURCE_DOES_NOT_EXIST = 1;

    /**
     * Status for when the specified target in the request does not exist.
     */
    public static final int TARGET_DOES_NOT_EXIST = 2;

    /**
     * Status for when there is currently no bound action for the specified request.
     */
    public static final int NO_SUCH_ACTION = 3;

    /**
     * The response status.
     */
    private int status;

    /**
     * Constructs a new {@link MenuActionResponse};
     *
     * @param status The status.
     */
    public MenuActionResponse(int status) {
        this.status = status;
    }

    /**
     * Get the response status.
     *
     * @return The status.
     */
    public int getStatus() {
        return status;
    }
}
