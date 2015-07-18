package io.mopar.rs2.file;

import io.mopar.file.FileSession;
import io.mopar.rs2.net.Attachment;

/**
 * @author Hadyn Fitzgerald
 */
public class FileSessionContext implements Attachment {

    /**
     * The session.
     */
    private FileSession session;

    /**
     *
     * @param session
     */
    public FileSessionContext(FileSession session) {
        this.session = session;
    }

    /**
     * Gets the file session.
     *
     * @return The file session.
     */
    public FileSession getSession() {
        return session;
    }
}