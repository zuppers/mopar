package io.mopar.cache;

import java.nio.ByteBuffer;

/**
 * @author Hadyn Fitzgerald
 */
public class ContainerHeader {

    /**
     * The compression.
     */
    private int compression;

    /**
     * The packed length.
     */
    private int packedLength;

    /**
     * The unpacked length.
     */
    private int unpackedLength;

    /**
     * Constructs a new {@link ContainerHeader);
     */
    public ContainerHeader() {}

    /**
     * Decodes a container header.
     *
     * @param buffer The buffer.
     */
    public void decode(ByteBuffer buffer) {
        compression = buffer.get() & 0xff;
        unpackedLength = packedLength = buffer.getInt();
        if(compression != Container.NONE) {
            unpackedLength = buffer.getInt();
        }
    }

    /**
     * Gets the compression.
     *
     * @return The compression.
     */
    public int getCompression() {
        return compression;
    }

    /**
     * Gets if the container is compressed.
     *
     * @return If the container is compressed.
     */
    public boolean isCompressed() {
        return compression != Container.NONE;
    }

    /**
     * Gets the packed length.
     *
     * @return The packed length.
     */
    public int getPackedLength() {
        return packedLength;
    }

    /**
     * Gets the unpacked length.
     *
     * @return The unpacked length.
     */
    public int getUnpackedLength() {
        return unpackedLength;
    }

    /**
     * Gets the length of the header in bytes.
     *
     * @return The length.
     */
    public int length() {
        return compression != Container.NONE ? 9 : 5;
    }
}
