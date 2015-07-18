package io.mopar.cache;

import io.mopar.util.BufferedFile;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by hadyn on 6/29/2015.
 */
public class Index {

    /**
     * The byte buffer used for reading and writing references.
     */
    private byte[] bytes = new byte[Reference.LENGTH];

    /**
     * The index file.
     */
    private BufferedFile file;

    /**
     * Constructs a new {@link Index};
     *
     * @param file The index file.
     */
    public Index(BufferedFile file) {
        this.file = file;
    }

    /**
     * Seeks to a reference.
     *
     * @param fileId The file id.
     * @throws IOException an I/O exception was encountered while seeking to a reference.
     */
    public void seek(int fileId) throws IOException {
        file.seek(Reference.LENGTH * fileId);
    }

    /**
     * Appends a reference.
     *
     * @param reference The reference to append.
     * @param fileId The file id.
     * @throws IOException an I/O exception was encountered while appending the reference.
     */
    public void write(Reference reference, int fileId) throws IOException {
        seek(fileId);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        reference.encode(buffer);

        file.write(bytes);
    }

    /**
     * Reads a reference.
     *
     * @param fileId The file id.
     * @return The read reference or <code>null</code> if the reference for the specified file does not exist.
     * @throws IOException an I/O exception was encountered while reading the reference.
     */
    public Reference read(int fileId) throws IOException {
        if(!exists(fileId)) {
            return null;
        }
        seek(fileId);

        file.read(bytes);

        Reference reference = new Reference();
        reference.decode(ByteBuffer.wrap(bytes));
        return reference;
    }

    /**
     * Gets if a reference exist.
     *
     * @param fileId The file id.
     * @return If the reference exists.
     */
    public boolean exists(int fileId) {
        return length() > fileId;
    }

    /**
     * Gets the amount of references contained in the index.
     *
     * @return The amount of references.
     */
    public int length() {
        return (int) ((file.length() + Reference.LENGTH - 1L) / (long) Reference.LENGTH);
    }
}
