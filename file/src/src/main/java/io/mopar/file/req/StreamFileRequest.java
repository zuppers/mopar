package io.mopar.file.req;

import io.mopar.core.Request;
import io.mopar.file.FileChunkStream;

/**
 * @author Hadyn Fitzgerald
 */
public class StreamFileRequest extends Request {

    /**
     * The session id.
     */
    private int sid;

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
     * The chunk stream.
     */
    private FileChunkStream stream;

    /**
     * Constructs a new {@link StreamFileRequest};
     *
     * @param sid The session id.
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @param priority If the file needs to be streamed immediately.
     */
    public StreamFileRequest(int sid, int volumeId, int fileId, boolean priority, FileChunkStream stream) {
        this.sid = sid;
        this.volumeId = volumeId;
        this.fileId = fileId;
        this.priority = priority;
        this.stream = stream;
    }

    /**
     * Gets the session id.
     *
     * @return The session id.
     */
    public int getSessionId() {
        return sid;
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
     * Gets if the file needs to be streamed immediately.
     *
     * @return If the request is considered a priority.
     */
    public boolean isPriority() {
        return priority;
    }

    /**
     * Gets the chunk stream.
     *
     * @return The stream.
     */
    public FileChunkStream getStream() {
        return stream;
    }
}
