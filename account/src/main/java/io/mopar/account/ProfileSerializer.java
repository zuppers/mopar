package io.mopar.account;

import io.mopar.account.res.SaveProfileResponse;
import io.mopar.core.Callback;

/**
 * @author Hadyn Fitzgerald
 */
public interface ProfileSerializer {

    /**
     *
     * @param profile
     * @param response
     */
    void save(Profile profile, Callback<SaveProfileResponse> response);
}
