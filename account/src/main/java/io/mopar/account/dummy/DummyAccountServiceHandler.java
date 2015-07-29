package io.mopar.account.dummy;

import io.mopar.account.AccountServiceHandler;
import io.mopar.account.Profile;
import io.mopar.account.req.*;
import io.mopar.account.res.*;
import io.mopar.core.Callback;


/**
 * Created by zuppers on 7/29/2015.
 */
public class DummyAccountServiceHandler implements AccountServiceHandler {


    @Override
    public void login(LoginRequest request, Callback<LoginResponse> callback) {
        loadProfile(request.getUsername(), (res) -> {
            LoginResponse response = new LoginResponse(LoginResponse.OK);
            switch (response.getStatus()) {
                case LoadProfileResponse.OK:
                    response.setProfile(res.getProfile());
                    break;
                // If we encounter an internal error report it back to the client and don't continue forward
                case LoadProfileResponse.INTERNAL_ERROR:
                    callback.call(new LoginResponse(LoginResponse.INTERNAL_ERROR));
                    return;
            }
            callback.call(response);
        });
    }

    @Override
    public void register(RegistrationRequest request, Callback<RegistrationResponse> callback) {

    }

    @Override
    public void loadProfile(LoadProfileRequest request, Callback<LoadProfileResponse> callback) {


            Profile profile = new Profile();
            profile.setX(3222);
            profile.setY(3222);

            LoadProfileResponse response = new LoadProfileResponse(LoadProfileResponse.OK);
            response.setProfile(profile);
            callback.call(response);
    }

    @Override
    public void saveProfile(SaveProfileRequest request, Callback<SaveProfileResponse> callback) {

    }

    @Override
    public void queryUsername(UsernameQueryRequest request, Callback<UsernameQueryResponse> callback) {

    }
}
