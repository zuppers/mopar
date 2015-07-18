package io.mopar.core.profile;

import io.mopar.core.Profile;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 *
 * Decodes profiles from provided binary data, assumes the data is in the expected format that this
 * decoder specifically takes responsibility for.
 */
public interface ProfileDecoder {

    /**
     * The default blank profile decoder.
     */
    ProfileDecoder DEFAULT = (data) -> new Profile();

    /**
     * Decodes a profile.
     *
     * @param data The profile data.
     * @return The decoded profile.
     * @throws IOException Thrown if there is an issue with decoding the profile data.
     */
    Profile decode(byte[] data) throws IOException;
}
