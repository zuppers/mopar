package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class StatusMessage extends Message {

    /**
     * The okay status message.
     */
    public static final StatusMessage OK = new StatusMessage(0, "status_ok");

    /**
     * The full status message for when a new service session failed to register.
     */
    public static final StatusMessage FULL = new StatusMessage(3, "status_full");

    /**
     * The out of date status message for when there was a client build mismatch.
     */
    public static final StatusMessage OUT_OF_DATE = new StatusMessage(6, "status_out_of_date");

    /**
     * The status id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * Constructs a new {@link StatusMessage};
     *
     * @param id The status id.
     * @param name The status name.
     */
    private StatusMessage(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
}
