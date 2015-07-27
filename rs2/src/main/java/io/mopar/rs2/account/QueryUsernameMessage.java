package io.mopar.rs2.account;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class QueryUsernameMessage extends Message {

    /**
     * The username.
     */
    private long username;

    /**
     * Constructs a new {@link QueryUsernameMessage};
     *
     * @param username the username.
     */
    public QueryUsernameMessage(long username) {
        this.username = username;
    }

    public long getUsername() {
        return username;
    }
}