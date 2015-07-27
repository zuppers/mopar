package io.mopar.account.res;

import io.mopar.core.Response;

/**
 * Created by hadyn on 7/26/2015.
 */
public class UsernameQueryResponse extends Response {
    public static final int ACCEPTED = -1;
    public static final int UNIQUE = 0;
    public static final int TAKEN = 1;
    public static final int INTERNAL_ERROR = -2;

    private int status;

    public UsernameQueryResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
