package io.mopar.game.model.state;

/**
 * @author Hadyn Fitzgerald
 *
 * The default states.
 */
public class States {

    /**
     * The idle entity state.
     */
    public static final int IDLE = 0;

    /**
     * The death entity state.
     */
    public static final int DEATH = 1;

    public static final int FRESH = 100;

    /**
     * Prevent instantiation;
     */
    private States() {}
}
