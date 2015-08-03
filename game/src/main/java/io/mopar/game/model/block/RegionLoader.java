package io.mopar.game.model.block;

/**
 * @author Hadyn Fitzgerald
 */
public interface RegionLoader {

    /**
     * Loads a region.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the loaded region.
     *
     * @throws RegionLoaderException
     */
    Region load(int x, int y) throws RegionLoaderException;
}
