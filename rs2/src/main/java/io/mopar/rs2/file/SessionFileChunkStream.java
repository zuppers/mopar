package io.mopar.rs2.file;

import io.mopar.file.FileChunk;
import io.mopar.file.FileChunkStream;
import io.mopar.rs2.net.Session;

/**
 * @author Hadyn Fitzgerald
 */
public class SessionFileChunkStream implements FileChunkStream {

    /**
     * The session.
     */
    private Session session;

    /**
     * Constructs a new {@link SessionFileChunkStream};
     *
     * @param session The session.
     */
    public SessionFileChunkStream(Session session) {
        this.session = session;
    }

    /**
     * Writes a chunk to the session.
     *
     * @param chunk The chunk.
     */
    @Override
    public void write(FileChunk chunk) {
        session.write(chunk);
    }

    /**
     * Closes the stream.
     */
    @Override
    public void close() {
        session.flush();
    }
}
