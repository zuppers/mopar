package io.mopar.world.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class SnapshotRequest extends Request {

    /**
     *
     */
    private int revision;

    /**
     *
     * @param revision
     */
    public SnapshotRequest(int revision) {
        this.revision = revision;
    }

    /**
     *
     * @return
     */
    public int getRevision() {
        return revision;
    }
}
