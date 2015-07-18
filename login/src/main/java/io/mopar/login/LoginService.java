package io.mopar.login;

import io.mopar.core.Callback;
import io.mopar.core.Service;
import io.mopar.login.req.LoginRequest;
import io.mopar.login.res.LoginResponse;

/**
 * @author Hadyn Fitzgerald
 */
public class LoginService extends Service {

    @Override
    public void setup() {
        setBlockForRequests(true);
        registerRequestHandlers();
    }

    @Override
    public void pulse() {

    }

    @Override
    public void teardown() {

    }

    /**
     * Registers all of the request handlers.
     */
    private void registerRequestHandlers() {
        registerRequestHandler(LoginRequest.class, this::handleLoginRequest);
    }

    /**
     *
     * @param callback
     */
    public void login(Callback<LoginResponse> callback) {
        submit(new LoginRequest(), callback);
    }

    /**
     * Handles a login request.
     *
     * @param request The login request.
     * @param callback The response callback.
     */
    private void handleLoginRequest(LoginRequest request, Callback callback) {
        callback.call(new LoginResponse(LoginResponse.OK));
    }
}
