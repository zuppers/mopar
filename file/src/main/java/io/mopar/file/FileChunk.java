package io.mopar.file;

/**
 * @author Hadyn Fitzgerald
 */
public class FileChunk {

    /**
     * The maximum length of a chunk.
     */
    public static final int LENGTH = 512;

    /**
     * The maximum amount of data in a chunk.
     */
    public static final int DATA_LENGTH = 511;

    /**
     * The buffer.
     */
    private byte[] buffer;

    /**
     * The length of the chunk.
     */
    private int length;

    /**
     * Constructs a new {@link FileChunk};
     *
     * @param buffer The buffer.
     * @param length The length of the chunk.
     */
    public FileChunk(byte[] buffer, int length) {
        this.buffer = buffer;
        this.length = length;
    }

    /**
     * Gets the chunk bytes.
     *
     * @return The bytes.
     */
    public byte[] getBytes() { return buffer; }

    /**
     * Gets the length.
     *
     * @return The length.
     */
    public int getLength() {
        return length;
    }
}
