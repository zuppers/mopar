package io.mopar.account.req;

import io.mopar.core.Request;

/**
 * Created by hadyn on 7/26/2015.
 */
public class UsernameQueryRequest extends Request {
    private long username;

    public UsernameQueryRequest(long username) {
        this.username = username;
    }

    public long getUsername() {
        return username;
    }
}
