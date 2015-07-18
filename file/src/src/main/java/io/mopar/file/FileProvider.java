package io.mopar.file;

import io.mopar.file.FileChunk;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public interface FileProvider {

    /**
     * Reads data for a file.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @param srcOff The offset in the source buffer.
     * @param dest The destination buffer.
     * @param destOff The offset in the destination buffer.
     * @param len The amount of bytes to read.
     * @return The amount of read bytes.
     */
    int read(int volumeId, int fileId, int srcOff, byte[] dest, int destOff, int len);

    /**
     * Gets a chunk for a file.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @param id The chunk id.
     * @param priority If the file is being requested as a priority.
     * @return The file chunk.
     */
    FileChunk getChunk(int volumeId, int fileId, int id, boolean priority) throws IOException;

    /***
     * Gets if a file exists.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @return If the file exists.
     */
    boolean exists(int volumeId, int fileId);

    /**
     * Gets the length of the file in chunks.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @return The length of the file in chunks.
     */
    int chunkedLength(int volumeId, int fileId) throws IOException;
}