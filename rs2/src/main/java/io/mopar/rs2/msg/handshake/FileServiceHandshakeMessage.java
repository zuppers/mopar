package io.mopar.rs2.msg.handshake;

import io.mopar.core.msg.Message;

/**
 * Created by hadyn on 6/25/2015.
 */
public class FileServiceHandshakeMessage extends Message {

    /**
     * The build.
     */
    private int build;

    /**
     * Constructs a new {@link FileServiceHandshakeMessage};
     *
     * @param build
     */
    public FileServiceHandshakeMessage(int build) {
        this.build = build;
    }

    /**
     * Gets the client build.
     *
     * @return The build.
     */
    public int getBuild() {
        return build;
    }
}
