package io.mopar.rs2.msg.world;

import io.mopar.core.msg.Message;
import io.mopar.world.Snapshot;

/**
 * @author Hadyn Fitzgerald
 */
public class SnapshotMessage extends Message {

    /**
     * The snapshot.
     */
    private Snapshot snapshot;

    /**
     *
     */
    private boolean worldsUpdated;

    /**
     *
     */
    private boolean populationUpdated;

    /**
     *
     * @param snapshot
     */
    public SnapshotMessage(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Gets the snapshot.
     *
     * @return The snapshot.
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     *
     * @param worldsUpdated
     */
    public void setWorldsUpdated(boolean worldsUpdated) {
        this.worldsUpdated = worldsUpdated;
    }

    /**
     *
     * @return
     */
    public boolean isWorldsUpdated() {
        return worldsUpdated;
    }

    /**
     *
     * @param populationUpdated
     */
    public void setPopulationUpdated(boolean populationUpdated) {
        this.populationUpdated = populationUpdated;
    }

    /**
     *
     * @return
     */
    public boolean isPopulationUpdated() {
        return populationUpdated;
    }
}
