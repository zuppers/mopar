package io.mopar.cache;

import java.nio.ByteBuffer;

import static io.mopar.util.ByteBufferUtil.getUnsignedMedium;
import static io.mopar.util.ByteBufferUtil.putMedium;

/**
 * @author Hadyn Fitzgerald
 */
public class Reference {

    /**
     * The length of a reference in bytes.
     */
    public static final int LENGTH = 6;

    /**
     * The length of the file.
     */
    private int length;

    /**
     * The first sector.
     */
    private int sector;

    /**
     * Constructs a new {@link Reference};
     */
    public Reference() {}

    /**
     * Constructs a new {@link Reference};
     *
     * @param length
     * @param sector
     */
    public Reference(int length, int sector) {
        this.length = length;
        this.sector = sector;
    }

    /**
     * Decodes a reference.
     *
     * @param buffer The buffer.
     */
    public void decode(ByteBuffer buffer) {
        length = getUnsignedMedium(buffer);
        sector = getUnsignedMedium(buffer);
    }

    /**
     * Encodes a reference.
     *
     * @param buffer The buffer.
     */
    public void encode(ByteBuffer buffer) {
        putMedium(buffer, length);
        putMedium(buffer, sector);
    }

    /**
     * Gets the length.
     *
     * @return The length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the first sector.
     *
     * @return The first sector.
     */
    public int getSectorId() {
        return sector;
    }

    /**
     *
     * @return
     */
    public boolean exists() {
        return sector != Sector.EOE;
    }
}
