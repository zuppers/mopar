package io.mopar.account.req;

/**
 * Created by hadyn on 7/27/2015.
 */
public class LoadProfileRequest {

    /**
     *
     */
    private long username;

    /**
     *
     * @param username
     */
    public LoadProfileRequest(long username) {
        this.username = username;
    }

    public long getUid() {
        return username;
    }
}
