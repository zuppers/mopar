package io.mopar.game.lua;

import io.mopar.game.model.Mobile;

/**
 * @author Hadyn Fitzgerald
 */
public class MobileComposite extends EntityComposite {

    /**
     *
     */
    private Mobile mobile;

    /**
     *
     * @param mobile
     */
    protected MobileComposite(Mobile mobile) {
        super(mobile);
        this.mobile = mobile;
    }

    /**
     *
     */
    public void toggle_run() {
        mobile.setRunning(!mobile.isRunning());
    }
}
