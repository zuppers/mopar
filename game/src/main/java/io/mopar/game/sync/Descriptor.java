package io.mopar.game.sync;

import io.mopar.game.model.Mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class Descriptor<T extends Mobile> {

    /**
     * The mobile.
     */
    protected T mobile;

    /**
     * The update blocks.
     */
    private Map<Class<? extends UpdateBlock>, UpdateBlock> updateBlocks = new HashMap<>();

    /**
     * Constructs a new {@link Descriptor};
     *
     * @param mobile The mobile.
     */
    protected Descriptor(T mobile) {
        this.mobile = mobile;
    }

    /**
     *
     * @return
     */
    public boolean hasUpdateBlocks() {
        return !updateBlocks.isEmpty();
    }
}
