package io.mopar.game.model;

import io.mopar.core.asset.AssetLoader;
import io.mopar.game.model.block.Region;
import io.mopar.game.model.block.RegionLoader;
import io.mopar.game.model.block.RegionLoaderException;

import java.io.ByteArrayInputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author Hadyn Fitzgerald
 */
public class AssetRegionLoader implements RegionLoader {

    /**
     * The asset loader.
     */
    private AssetLoader assetLoader;

    /**
     *
     * @param assetLoader
     */
    public AssetRegionLoader(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    /**
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return
     *
     * @throws RegionLoaderException
     */
    @Override
    public Region load(int x, int y) throws RegionLoaderException {
        try {
            Region region = new Region(x, y);
            byte[] bytes = assetLoader.load("regions/r" + x + "_" + y);
            region.parse(new GZIPInputStream(new ByteArrayInputStream(bytes)));
            return region;
        } catch (Exception ex) {
            throw new RegionLoaderException(ex);
        }
    }
}
