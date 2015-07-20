package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class PrintMessage extends Message {

    /**
     * The text.
     */
    private String text;

    /**
     * Constructs a new {@link PrintMessage};
     *
     * @param text the text.
     */
    public PrintMessage(String text) {
        this.text = text;
    }

    /***
     * Gets the text.
     *
     * @return the text.
     */
    public String getText() {
        return text;
    }
}
