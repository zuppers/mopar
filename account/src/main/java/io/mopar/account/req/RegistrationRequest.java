package io.mopar.account.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class RegistrationRequest extends Request {
    private long username;
    private String password;

    public RegistrationRequest(long username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
