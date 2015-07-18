package io.mopar.file;

/**
 * @author Hadyn Fitzgerald
 */
public class FileRequest {

    /**
     * The volume id.
     */
    private int volumeId;

    /**
     * The file id.
     */
    private int fileId;

    /**
     * The priority flag.
     */
    private boolean priority;

    /**
     * The stream.
     */
    private FileChunkStream stream;

    /**
     * The current chunk being written.
     */
    private int chunk;

    /**
     * Constructs a new {@link FileRequest};
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @param priority The priority.
     */
    public FileRequest(int volumeId, int fileId, boolean priority, FileChunkStream stream) {
        this.volumeId = volumeId;
        this.fileId = fileId;
        this.priority = priority;
        this.stream = stream;
    }

    /**
     * Gets the volume id.
     *
     * @return The volume id.
     */
    public int getVolumeId() {
        return volumeId;
    }

    /**
     * Gets the file id.
     *
     * @return The file id.
     */
    public int getFileId() {
        return fileId;
    }

    /**
     * Gets if the request is a priority.
     *
     * @return If the request is a priority.
     */
    public boolean isPriority() {
        return priority;
    }

    /**
     * Gets and increments the chunk being written.
     *
     * @return The current chunk to write.
     */
    public int incrementChunk() {
        return chunk++;
    }

    /**
     * Gets the chunk.
     *
     * @return The chunk.
     */
    public int getChunk() {
        return chunk;
    }

    /**
     * Writes out a chunk to the request stream.
     *
     * @param chunk The chunk.
     */
    public void write(FileChunk chunk) {
        stream.write(chunk);
    }

    /**
     * Closes the stream.
     */
    public void close() {
        stream.close();
    }
}
