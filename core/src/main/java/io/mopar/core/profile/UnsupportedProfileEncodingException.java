package io.mopar.core.profile;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public class UnsupportedProfileEncodingException extends IOException {

    /**
     * Constructs a new {@link UnsupportedProfileEncodingException};
     *
     * @param encoding The unsupported profile encoding.
     */
    public UnsupportedProfileEncodingException(ProfileEncoding encoding) {
        super(encoding.name());
    }
}
