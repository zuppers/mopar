package io.mopar.account;

import java.nio.ByteBuffer;

/**
 * Created by hadyn on 7/27/2015.
 */
public class DefaultProfileCodec implements ProfileCodec {
    @Override
    public Profile decode(byte[] bytes) throws MalformedProfileException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        Profile profile = new Profile(buffer.getLong());

        int hash = buffer.getInt();
        profile.setX(hash >> 14 & 0x1fff);
        profile.setY(hash & 0x1fff);
        profile.setPlane(hash >> 28 & 0x3);

        return profile;
    }

    @Override
    public byte[] encode(Profile profile) throws MalformedProfileException {
        int length = 8 + 4;

        byte[] bytes = new byte[length];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putLong(profile.getUid());
        buffer.putInt(profile.getPlane() << 28 | profile.getX() << 14 | profile.getY());

        return bytes;
    }
}
