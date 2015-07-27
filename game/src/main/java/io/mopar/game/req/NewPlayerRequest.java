package io.mopar.game.req;

import io.mopar.account.Profile;
import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class NewPlayerRequest extends Request {

    private long username;
    private Profile profile;

    /**
     * Constructs a new {@link NewPlayerRequest};
     */
    public NewPlayerRequest(long username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public long getUsername() {
        return username;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public boolean hasProfile() {
        return profile != null;
    }
}
