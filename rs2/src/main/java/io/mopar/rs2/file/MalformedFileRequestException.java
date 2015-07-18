package io.mopar.rs2.file;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public class MalformedFileRequestException extends IOException {

    /**
     * Constructs a new {@link MalformedFileRequestException};
     */
    public MalformedFileRequestException() {}

    /**
     * Constructs a new {@link MalformedFileRequestException};
     */
    public MalformedFileRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@link MalformedFileRequestException};
     */
    public MalformedFileRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@link MalformedFileRequestException};
     */
    public MalformedFileRequestException(Throwable cause) {
        super(cause);
    }
}
