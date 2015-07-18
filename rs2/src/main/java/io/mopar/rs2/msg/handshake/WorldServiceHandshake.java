package io.mopar.rs2.msg.handshake;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldServiceHandshake extends Message {

    /**
     * The list revision.
     */
    private int revision;

    /**
     * Constructs a new {@link WorldServiceHandshake};
     *
     * @param revision The list revision.
     */
    public WorldServiceHandshake(int revision) {
        this.revision = revision;
    }

    /**
     * Gets the list revision.
     *
     * @return The revision.
     */
    public int getRevision() {
        return revision;
    }
}
