package io.mopar.game.sync;

import io.mopar.game.model.Mobile;

import java.util.*;

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
     * @param updateBlock
     */
    public void addUpdateBlock(UpdateBlock updateBlock) {
        updateBlocks.put(updateBlock.getClass(), updateBlock);
    }

    /**
     *
     * @param blockClass
     * @param <T>
     * @return
     */
    public <T extends UpdateBlock> boolean hasUpdateBlock(Class<T> blockClass) {
        return updateBlocks.containsKey(blockClass);
    }

    /**
     *
     * @param blockClass
     * @param <T>
     * @return
     */
    public <T extends UpdateBlock> T getUpdateBlock(Class<T> blockClass) {
        return (T) updateBlocks.get(blockClass);
    }

    /**
     *
     * @return
     */
    public boolean hasUpdateBlocks() {
        return !updateBlocks.isEmpty();
    }

    /**
     *
     * @return
     */
    public Collection<UpdateBlock> getBlocks() {
        return updateBlocks.values();
    }
}
