package io.mopar.account.req;

import io.mopar.account.Profile;
import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class SaveProfileRequest extends Request {

    /**
     *
     */
    private Profile profile;

    /**
     *
     * @param profile
     */
    public SaveProfileRequest(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}
