package io.mopar.game.model.block;

import io.mopar.game.model.Direction;
import io.mopar.game.model.Position;
import io.mopar.game.model.TraversalMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class RegionSet {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RegionSet.class);

    /**
     * The regions.
     */
    private Map<Integer, Region> regions = new HashMap<>();

    /**
     * The region loader.
     */
    private RegionLoader loader = (x, y) -> new Region(x, y);

    /**
     * The touch timeout.
     */
    private long touchTimeout = 30000L;

    /**
     * Constructs a new {@link RegionSet};
     */
    public RegionSet() {}

    /**
     * Sets the loader.
     *
     * @param loader the loader.
     */
    public void setLoader(RegionLoader loader) {
        this.loader = loader;
    }

    /**
     * Processes all of the active regions.
     */
    public void reset() {
        Iterator<Region> itr = regions.values().iterator();
        while(itr.hasNext()) {
            Region region = itr.next();

            // Check if the region has fallen to become inactive
            long diff = System.currentTimeMillis() - region.getLastTouchedTime();
            if(diff > touchTimeout) {
                logger.info("Region has been marked as inactive, unloading; " + region.getX() + "-" + region.getY());
                itr.remove();
                continue;
            }

            // Reset the region
            region.reset();
        }
    }

    /**
     * Loads a region.
     *
     * @param x the region x coordinate.
     * @param y the region y coordinate.
     */
    public void load(int x, int y) {
        if(!isLoaded(x, y)) {
            safelyLoad(x, y);
        }
    }

    /**
     * Gets a region. If the region is not in memory, safely loads the region.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return
     */
    public Region get(int x, int y) {
        if(!isLoaded(x, y)) {
            safelyLoad(x, y);
        }
        Region region = regions.get(getRegionKey(x, y));
        region.touch();
        return region;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private Region safelyLoad(int x, int y) {
        try {
            logger.info("Loading region " + x + "-" + y);
            Region region = loader.load(x, y);
            regions.put(getRegionKey(x, y), region);
            return region;
        } catch (RegionLoaderException ex) {
            logger.error("Failed to load requested region, using blank template instead", ex);
            return new Region(x, y);
        }
    }

    /**
     * Gets if a region is loaded.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return if the region is loaded into memory.
     */
    public boolean isLoaded(int x, int y) {
       return regions.containsKey(getRegionKey(x, y));
    }

    /**
     *
     * @param plane
     * @param blockX
     * @param blockY
     * @return
     */
    public boolean isBlockUpdated(int plane, int blockX, int blockY) {
        int regionX = blockX >> 3, regionY = blockY >> 3;
        if(!isLoaded(regionX, regionY)) {
            return false;
        }
        Region region = get(regionX, regionY);
        Block block =  region.getBlock(plane,
                blockX - (regionX << 3),
                blockY - (regionY << 3));
        return block.isUpdated();
    }

    /**
     *
     * @param position
     * @return
     */
    public Block getBlock(Position position) {
        return getBlock(position.getPlane(),
                position.getBlockX(),
                position.getBlockY());
    }

    /**
     *
     * @param plane
     * @param blockX
     * @param blockY
     * @return
     */
    public Block getBlock(int plane, int blockX, int blockY) {
        int regionX = blockX >> 3, regionY = blockY >> 3;
        Region region = get(regionX, regionY);
        return region.getBlock(plane,
                blockX - (regionX << 3),
                blockY - (regionY << 3));
    }

    /**
     *
     * @param position
     * @param direction
     * @param tflags
     * @return
     */
    public boolean isTraversable(Position position, Direction direction, int tflags) {
        Region region = get(position.getRegionX(), position.getRegionY());
        TraversalMap traversalMap = region.getTraversalMap(position.getPlane());
        return traversalMap.isTraversable(position.getLocalRegionX(), position.getLocalRegionY(), direction, tflags);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private static final int getRegionKey(int x, int y) {
        return (x & 0xff) << 8 | (y & 0xff);
    }
}