package io.mopar.account;

import io.mopar.account.req.LoginRequest;
import io.mopar.account.req.RegistrationRequest;
import io.mopar.account.req.SaveProfileRequest;
import io.mopar.account.req.UsernameQueryRequest;
import io.mopar.account.res.LoginResponse;
import io.mopar.account.res.RegistrationResponse;
import io.mopar.account.res.SaveProfileResponse;
import io.mopar.account.res.UsernameQueryResponse;
import io.mopar.core.Callback;
import io.mopar.core.Service;

/**
 * @author Hadyn Fitzgerald
 */
public class AccountService extends Service {

    /**
     * The handler.
     */
    private AccountServiceHandler handler;

    /**
     *
     * @param handler
     */
    public void setHandler(AccountServiceHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setup() throws Exception {
        setBlockForRequests(true);
        registerRequestHandlers();
    }

    @Override
    public void pulse() throws Exception {}

    @Override
    public void teardown() {}

    /**
     *
     * @param username
     * @param callback
     */
    public void queryUsername(long username, Callback<UsernameQueryResponse> callback) {
        submit(new UsernameQueryRequest(username), callback);
    }

    /**
     *
     * @param username
     * @param password
     * @param callback
     */
    public void register(long username, String password, Callback<RegistrationResponse> callback) {
        submit(new RegistrationRequest(username, password), callback);
    }

    /**
     *
     * @param username
     * @param password
     * @param callback
     */
    public void login(long username, String password, Callback<LoginResponse> callback) {
        submit(new LoginRequest(username, password), callback);
    }

    /**
     *
     * @param profile
     * @param callback
     */
    public void saveProfile(Profile profile, Callback<SaveProfileResponse> callback) {
        submit(new SaveProfileRequest(profile), callback);
    }

    /**
     *
     */
    private void registerRequestHandlers() {
        registerRequestHandler(UsernameQueryRequest.class, this::handleUsernameQueryRequest);
        registerRequestHandler(RegistrationRequest.class, this::handleRegistrationRequest);
        registerRequestHandler(LoginRequest.class, this::handleLoginRequest);
        registerRequestHandler(SaveProfileRequest.class, this::handleSaveProfileRequest);
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleUsernameQueryRequest(UsernameQueryRequest request, Callback callback) {
        callback.call(new UsernameQueryResponse(UsernameQueryResponse.ACCEPTED));
        handler.queryUsername(request, callback);
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleRegistrationRequest(RegistrationRequest request, Callback callback) {
        callback.call(new RegistrationResponse(RegistrationResponse.ACCEPTED));
        handler.register(request, callback);
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleLoginRequest(LoginRequest request, Callback callback) {
        callback.call(new LoginResponse(LoginResponse.ACCEPTED));
        handler.login(request, callback);
    }

    /**
     *
     * @param request
     * @param callback
     */
    private void handleSaveProfileRequest(SaveProfileRequest request, Callback callback) {
        callback.call(new SaveProfileResponse(SaveProfileResponse.ACCEPTED));
        handler.saveProfile(request, callback);
    }
}
