package io.mopar.game.config;

/**
 * @author Hadyn Fitzgerald
 */
public enum ActionAccess {

    /**
     *
     */
    OPTION1(1 << 1);

    /**
     * The flag.
     */
    private int flag;

    /**
     *
     * @param flag
     */
    ActionAccess(int flag) {
        this.flag = flag;
    }

    /**
     *
     * @return
     */
    public int getFlag() {
        return flag;
    }
}
