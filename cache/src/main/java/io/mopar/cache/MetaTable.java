package io.mopar.cache;

import java.nio.ByteBuffer;

/**
 * @author Hadyn Fitzgerald
 */
public class MetaTable {

    /**
     * The version.
     */
    private int version;

    /**
     * The revision.
     */
    private int revision;

    /**
     * Constructs a new {@link MetaTable};
     */
    public MetaTable() {}

    /**
     * Decodes a table.
     *
     * @param bytes The bytes.
     */
    public void decode(byte[] bytes) {
        decode(bytes, 0, bytes.length);
    }

    /**
     * Decodes a table.
     *
     * @param bytes The bytes.
     * @param off The offset in the bytes.
     * @param len The amount of bytes.
     */
    public void decode(byte[] bytes, int off, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, off, len);

        // Read the table version
        version = buffer.get() & 0xff;
        if(version != 5 && version != 6) {
            throw new RuntimeException("Invalid version: " + version);
        }

        // For table versions 6 or greater read the revision
        if(version > 5) {
            revision = buffer.getInt();
        } else {
            revision = 0;
        }
    }

    /**
     * Gets the revision.
     *
     * @return The revision.
     */
    public int getRevision() {
        return revision;
    }
}
