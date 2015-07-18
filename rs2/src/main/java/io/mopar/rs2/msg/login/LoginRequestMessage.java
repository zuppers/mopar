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
     *
     * @param username
     */
    public void setUsername(long username) {
        this.username = username;
    }

    /**
     *
     * @param password
     * @return
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param displayMode
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
     *
     * @return
     */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
     *
     * @param cipherKeys
     */
    public void setCipherKeys(int[] cipherKeys) {
        this.cipherKeys = cipherKeys;
    }

    /**
     *
     * @return
     */
    public int[] getCipherKeys() {
        return cipherKeys;
    }
}
