package io.mopar.account.res;

import io.mopar.core.Response;

/**
 * Created by hadyn on 7/27/2015.
 */
public class SaveProfileResponse extends Response {
    public static final int ACCEPTED = 0;
    public static final int OK = 1;
    public static final int INTERNAL_ERROR = 2;

    private int status;

    public SaveProfileResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
