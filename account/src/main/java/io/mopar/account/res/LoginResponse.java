package io.mopar.account.res;

import io.mopar.account.Profile;
import io.mopar.core.Response;

/**
 * Created by hadyn on 7/27/2015.
 */
public class LoginResponse extends Response {

    public static final int INVALID_USER_OR_PASS = 1;
    public static final int OK = 0;
    public static final int INTERNAL_ERROR = 2;
    public static final int ACCEPTED = 3;
    private int status;
    private Profile profile;

    public LoginResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}
