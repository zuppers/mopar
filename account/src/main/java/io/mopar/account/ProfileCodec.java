package io.mopar.account;

/**
 * Created by hadyn on 7/27/2015.
 */
public interface ProfileCodec {

    /**
     *
     * @param bytes
     */
    Profile decode(byte[] bytes) throws MalformedProfileException;

    /**
     *
     * @param profile
     * @return
     */
    byte[] encode(Profile profile) throws MalformedProfileException;
}
