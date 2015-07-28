package io.mopar.account;

/**
 * @author Hadyn Fitzgerald
 */
public interface ProfileCodec {

    /**
     *
     * @param bytes
     */
    Profile decode(byte[] bytes) throws MalformedProfileException;

    /**
     * Encodes a profile into the profile file format.
     *
     * @param profile
     * @return
     */
    byte[] encode(Profile profile) throws MalformedProfileException;
}
