package io.mopar.rs2.msg.login;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class LoginRequestMessage extends Message {

    /**
     * The build.
     */
    private int build = -1;

    /**
     * The username.
     */
    private long username = -1L;

    /**
     * The password.
     */
    private String password = "";

    /**
     * Reconnecting flag.
     */
    private boolean reconnecting;

    /**
     * The display mode.
     */
    private int displayMode;

    /**
     * The keys.
     */
    private int[] cipherKeys;

    /**
     * Constructs a new {@link LoginRequestMessage};
     */
    public LoginRequestMessage() {}

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(long username) {
        this.username = username;
    }

    /**
     * Gets the username.
     *
     * @return the username.
     */
    public long getUsername() {
        return username;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     * @return the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the display mode.
     *
     * @param displayMode the display mode.
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
     * Gets the display mode.
     *
     * @return the display mode.
     */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
     * Sets the cipher keys.
     *
     * @param cipherKeys the cipher keys.
     */
    public void setCipherKeys(int[] cipherKeys) {
        this.cipherKeys = cipherKeys;
    }

    /**
     * Gets the cipher keys.
     *
     * @return the cipher keys.
     */
    public int[] getCipherKeys() {
        return cipherKeys;
    }

    public String getPassword() {
        return password;
    }
}