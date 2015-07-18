package io.mopar.rs2.net.packet;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public class MalformedPacketException extends IOException {
    public MalformedPacketException() {}
    public MalformedPacketException(String message) {
        super(message);
    }
    public MalformedPacketException(String message, Throwable cause) {
        super(message, cause);
    }
    public MalformedPacketException(Throwable cause) {
        super(cause);
    }
}
