package io.mopar.game.req;

import io.mopar.core.Request;
import io.mopar.core.profile.ProfileEncoding;

/**
 * @author Hadyn Fitzgerald
 */
public class NewPlayerRequest extends Request {

    /**
     * The profile encoding format.
     */
    private ProfileEncoding encoding;

    /**
     * The profile data.
     */
    private byte[] profileData;

    /**
     * Constructs a new {@link NewPlayerRequest};
     */
    public NewPlayerRequest() {}

    /**
     * Gets the profile data.
     *
     * @return The profile data.
     */
    public byte[] getProfileData() { return profileData; }

    /**
     * Helper method; gets if the request has data for the player profile.
     *
     * @return If the profile data is not <code>null</code>.
     */
    public boolean hasProfileData() {
        return profileData != null;
    }

    /**
     * Gets the profile encoding format.
     *
     * @return The profiling encoding.
     */
    public ProfileEncoding getEncoding() {
        return encoding;
    }
}
