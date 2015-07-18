package io.mopar.world.res;

import io.mopar.core.Response;
import io.mopar.world.Snapshot;

/**
 * @author Hadyn Fitzgerald
 */
public class SnapshotResponse extends Response {

    public static final int WORLDS_UPDATED = 0x1;
    public static final int POPULATION_UPDATED = 0x2;

    /**
     *
     */
    public static final int OK = 0;

    /**
     *
     */
    private int status;

    /**
     *
     */
    private Snapshot snapshot;

    /**
     *
     */
    private int flags;

    /**
     *
     * @param status
     */
    public SnapshotResponse(int status) {
        this.status = status;
    }

    /**
     *
     * @param snapshot
     */
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     *
     * @return
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     *
     * @param flag
     */
    public void setFlag(int flag) {
        flags |= flag;
    }

    /**
     *
     * @param flag
     * @return
     */
    public boolean isActive(int flag) {
        return (flags & flag) != 0;
    }

    /**
     *
     * @return
     */
    public int getFlags() {
        return flags;
    }
}
