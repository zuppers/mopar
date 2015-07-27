package io.mopar.account.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class RegistrationResponse extends Response {
    public static final int OK = 0;
    public static final int USER_ALREADY_EXISTS = 1;
    public static final int INTERNAL_ERROR = 2;
    public static final int ACCEPTED = -1;
    private int status;

    public RegistrationResponse(int status) {

    }

    public int getStatus() {
        return status;
    }
}
