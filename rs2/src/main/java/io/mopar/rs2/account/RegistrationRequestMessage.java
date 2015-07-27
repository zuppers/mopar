package io.mopar.rs2.account;

import io.mopar.core.msg.Message;

/**
 * Created by hadyn on 7/27/2015.
 */
public class RegistrationRequestMessage extends Message {

    private long username;
    private String password;

    public RegistrationRequestMessage() {}

    public void setUsername(long username) {
        this.username = username;
    }

    public long getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
