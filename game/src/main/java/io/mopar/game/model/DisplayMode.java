package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class DisplayMode {

    public static final int MODE_FIXED_SD       = 0;
    public static final int MODE_FIXED_HD       = 1;
    public static final int MODE_RESIZABLE_HD   = 2;
    public static final int MODE_FULLSCREEN_HD  = 3;

    /**
     *
     * @param mode
     * @return
     */
    public static boolean isFixedView(int mode) {
        switch (mode) {
            case MODE_FIXED_HD:
            case MODE_FIXED_SD:
                return true;
            default:
                return false;
        }
    }

    /**
     *
     */
    private DisplayMode() {}
}
