package io.mopar.file.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class RemoveSessionResponse extends Response {
    public static final int OK = 0;
    public static final int SESSION_DOES_NOT_EXIST = 1;

    private int status;

    public RemoveSessionResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
