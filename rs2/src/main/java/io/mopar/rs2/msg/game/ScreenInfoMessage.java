package io.mopar.rs2.msg.game;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class ScreenInfoMessage extends Message {

    /**
     * The display mode.
     */
    private int displayMode;

    private int screenWidth;
    private int screenHeight;
    private int numberSamples;

    public ScreenInfoMessage(int displayMode, int screenWidth, int screenHeight, int numberSamples) {
        this.displayMode = displayMode;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.numberSamples = numberSamples;
    }

    public int getDisplayMode() {
        return displayMode;
    }
}
