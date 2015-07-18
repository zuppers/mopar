package io.mopar.core.profile;

import io.mopar.core.Profile;

import java.io.IOException;
import java.util.EnumMap;

/**
 * @author Hadyn Fitzgerald
 */
public class ProfileCodec {

    /**
     * The profile decoders.
     */
    private EnumMap<ProfileEncoding, ProfileDecoder> decoders = new EnumMap<>(ProfileEncoding.class);

    /**
     * Registers a decoder.
     *
     * @param encoding The profile encoding.
     * @param deserializer The profile deserializer.
     */
    public void registerDecoder(ProfileEncoding encoding, ProfileDecoder deserializer) {
        decoders.put(encoding, deserializer);
    }

    /**
     * Decodes a profile.
     *
     * @param encoding The profile encoding.
     * @param data The profile data.
     * @throws IOException An I/O exception is encountered, either the provided encoding is unsupported
     *                      or there was an error while deserializing the profile data.
     */
    public Profile decode(ProfileEncoding encoding, byte[] data) throws IOException {
        if(!decoders.containsKey(encoding)) {
            throw new UnsupportedProfileEncodingException(encoding);
        }
        ProfileDecoder decoder = decoders.get(encoding);
        return decoder.decode(data);
    }
}
