package io.mopar.file;

/**
 * @author Hadyn Fitzgerald
 */
public interface FileChunkStream {

    /**
     * Writes a chunk to the stream.
     *
     * @param chunk The chunk.
     */
    void write(FileChunk chunk);

    /**
     * Closes the stream.
     */
    void close();
}
