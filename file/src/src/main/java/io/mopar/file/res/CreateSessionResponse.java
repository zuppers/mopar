package io.mopar.file.res;

import io.mopar.core.Response;
import io.mopar.file.FileSession;

/**
 * Created by hadyn on 6/28/2015.
 */
public class CreateSessionResponse extends Response {

    public static final int OK = 0;
    public static final int FULL = 1;

    /**
     * The status.
     */
    private int status;

    /**
     * The session.
     */
    private FileSession session;

    /**
     * Constructs a new {@link CreateSessionResponse};
     *
     * @param status The status.
     */
    public CreateSessionResponse(int status) {
        this.status = status;
    }

    /**
     * Gets the response status.
     *
     * @return The status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the created session.
     *
     * @param session The session.
     */
    public void setSession(FileSession session) {
        this.session = session;
    }

    /**
     * Gets the created session.
     *
     * @return The session.
     */
    public FileSession getSession() {
        return session;
    }
}
