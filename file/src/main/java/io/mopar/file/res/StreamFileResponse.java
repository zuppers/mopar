package io.mopar.file.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class StreamFileResponse extends Response {

    public static final int OK = 0;
    public static final int SESSION_DOES_NOT_EXIST = 1;
    public static final int FILE_DOES_NOT_EXIST = 2;
    public static final int REACHED_REQUEST_LIMIT = 3;

    /**
     * The response status.
     */
    private int status;

    /**
     * Constructs a new {@link StreamFileResponse};
     *
     * @param status The status.
     */
    public StreamFileResponse(int status) {
        this.status = status;
    }
}
