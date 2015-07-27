package io.mopar.account.res;

import io.mopar.account.Profile;
import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class LoadProfileResponse extends Response {
    public static final int INTERNAL_ERROR = 1;
    public static final int OK = 0;
    public static final int PROFILE_DOES_NOT_EXIST = 3;

    private int status;
    private Profile profile;

    public LoadProfileResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
