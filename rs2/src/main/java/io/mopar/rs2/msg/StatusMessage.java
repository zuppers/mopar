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
     *
     */
    public static final StatusMessage INVALID_USER_OR_PASS = new StatusMessage(3, "status_invalid_user_or_pass");


    /**
     * The out of date status message for when there was a client build mismatch.
     */
    public static final StatusMessage OUT_OF_DATE = new StatusMessage(6, "status_out_of_date");


    /**
     * The full status message for when a new service session failed to register.
     */
    public static final StatusMessage FULL = new StatusMessage(7, "status_full");
    public static final StatusMessage CONTINUE = new StatusMessage(2, "status_continue");
    public static final StatusMessage ALREADY_ONLINE = new StatusMessage(5, "status_already_online");

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
