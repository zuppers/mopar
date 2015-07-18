package io.mopar.login.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class LoginResponse extends Response {
    public static final int OK = 0;
    public static final int INVALID_CREDENTIALS = 1;
    public static final int ALREADY_LOGGED_IN = 2;
    public static final int TOO_MANY_FAILED_ATTEMPTS = 3;

    private int status;

    public LoginResponse(int status) {
        this.status = status;
    }
}
