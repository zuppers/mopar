package io.mopar.file.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class RemoveSessionRequest extends Request {

    /**
     *
     */
    private int sessionId;

    /**
     *
     * @param sessionId
     */
    public RemoveSessionRequest(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }
}
