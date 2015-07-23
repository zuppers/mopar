package io.mopar.game.model;

import java.util.Arrays;

/**
 * @author Hadyn Fitzgerald
 */
public class Appearance {

    public static final int HEAD = 0;
    public static final int BEARD = 1;
    public static final int BODY = 2;
    public static final int ARMS = 3;
    public static final int HANDS = 4;
    public static final int LEGS = 5;
    public static final int FEET = 6;
    public static final int UNUSED = 7;

    /**
     * The styles.
     */
    private int[] styles = new int[8];

    /**
     * The flags for if features are visible.
     */
    private boolean[] visible = new boolean[8];

    /**
     * Flag for the gender.
     */
    private boolean male;

    /**
     * Constructs a new {@link Appearance};
     */
    public Appearance() {
        setStyle(HEAD, 1);
        setStyle(BEARD, 10);
        setStyle(BODY, 19);
        setStyle(ARMS, 27);
        setStyle(HANDS, 33);
        setStyle(LEGS, 37);
        setStyle(FEET, 42);

        Arrays.fill(visible, true);
    }

    /**
     *
     * @param bool
     */
    public void setMale(boolean bool) {
        this.male = male;
    }

    /**
     * Gets if the gender is male.
     *
     * @return if the gender is male
     */
    public boolean isMale() {
        return male;
    }

    /**
     * Sets a style.
     *
     * @param slot the slot.
     * @param value the value.
     */
    public void setStyle(int slot, int value) {
        styles[slot] = value;
    }

    /**
     * Gets a style.
     *
     * @param slot the slot.
     * @return the style.
     */
    public int getStyle(int slot) {
        return styles[slot];
    }

    /**
     * Gets if a feature is visible.
     *
     * @param slot the slot.
     * @return if the feature is visible.
     */
    public boolean isVisible(int slot) {
        return visible[slot];
    }
}