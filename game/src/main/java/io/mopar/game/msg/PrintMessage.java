package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class PrintMessage extends Message {

    /**
     * The message.
     */
    private String text;

    /**
     *
     * @param text
     */
    public PrintMessage(String text) {
        this.text = text;
    }

    /***
     * Gets the message.
     *
     * @return the string.
     */
    public String getText() {
        return text;
    }
}
