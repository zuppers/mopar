package io.mopar.game.lua;

import io.mopar.core.asset.AssetLoader;
import io.mopar.core.asset.AssetLoaderException;
import io.mopar.core.lua.LuaModule;

import java.net.URISyntaxException;

/**
 * @author Hadyn Fitzgerald
 */
public class AssetModule implements LuaModule {

    private static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * The asset loader.
     */
    private AssetLoader assetLoader;

    /**
     * Constructs a new {@link AssetModule};
     *
     * @param assetLoader the asset loader.
     */
    public AssetModule(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    /**
     * Loads an asset from its provided uniform resource identifier.
     *
     * @param uri the uri for the asset.
     * @return the data for the asset as a byte array.
     *
     * @throws AssetLoaderException
     * @throws URISyntaxException
     */
    public byte[] Load(String uri) throws AssetLoaderException, URISyntaxException {
        return assetLoader.load(uri);
    }

    /**
     * Attempts to load an asset.
     *
     * @param uri the uri for the asset.
     * @return the data for the asset as a byte array or {@code null} if an exception was encountered.
     */
    public byte[] TryLoad(String uri) {
        try {
            return assetLoader.load(uri);
        } catch (AssetLoaderException | URISyntaxException ex) {
            return null;
        }
    }

    @Override
    public String getNamespace() {
        return "asset";
    }
}
