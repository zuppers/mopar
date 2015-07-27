package io.mopar.account;

import io.mopar.account.res.SaveProfileResponse;
import io.mopar.core.Callback;

/**
 * Created by hadyn on 7/27/2015.
 */
public class ServiceProfileSerializer implements ProfileSerializer {

    private AccountService service;

    public ServiceProfileSerializer(AccountService service) {
        this.service = service;
    }

    @Override
    public void save(Profile profile, Callback<SaveProfileResponse> callback) {
        service.saveProfile(profile, callback);
    }
}
