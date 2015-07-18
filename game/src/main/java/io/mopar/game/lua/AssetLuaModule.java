package io.mopar.game.lua;

import io.mopar.core.lua.LuaModule;
import io.mopar.core.asset.AssetLoader;
import io.mopar.core.asset.AssetLoaderException;

import java.net.URISyntaxException;

/**
 * @author Hadyn Fitzgerald
 */
public class AssetLuaModule implements LuaModule {

    private AssetLoader loader;

    /**
     * Constructs a new {@link AssetLuaModule};
     *
     * @param loader The asset loader.
     */
    public AssetLuaModule(AssetLoader loader) {
        this.loader = loader;
    }

    /**
     * Loads an asset.
     *
     * @param uri The unique resource identifier of the asset to load.
     * @return The asset data.
     * @throws AssetLoaderException
     * @throws URISyntaxException
     */
    public byte[] load(String uri) throws AssetLoaderException, URISyntaxException {
        return loader.load(uri);
    }

    @Override
    public String getNamespace() {
        return "asset";
    }
}
