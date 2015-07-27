package io.mopar.account;

import io.mopar.account.req.*;
import io.mopar.account.res.*;
import io.mopar.core.Callback;

/**
 * @author Hadyn Fitzgerald
 */
public interface AccountServiceHandler {

    void login(LoginRequest request, Callback<LoginResponse> callback);

    /**
     *
     * @param request
     * @param callback
     */
    void register(RegistrationRequest request, Callback<RegistrationResponse> callback);

    /**
     *
     * @param username
     * @param callback
     */
    default void loadProfile(long username, Callback<LoadProfileResponse> callback) {
        loadProfile(new LoadProfileRequest(username), callback);
    }

    /**
     *
     * @param request
     * @param callback
     */
    void loadProfile(LoadProfileRequest request, Callback<LoadProfileResponse> callback);


    /**
     *
     * @param request
     * @param callback
     */
    void saveProfile(SaveProfileRequest request, Callback<SaveProfileResponse> callback);

    /**
     *
     * @param username
     * @param callback
     */
    default void queryUsername(long username, Callback<UsernameQueryResponse> callback) {
        queryUsername(new UsernameQueryRequest(username), callback);
    }

    /**
     * Queries if a username is unique.
     *
     * @param request the request.
     * @param callback the callback.
     */
    void queryUsername(UsernameQueryRequest request, Callback<UsernameQueryResponse> callback);

}