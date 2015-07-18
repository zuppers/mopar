package io.mopar.core.asset;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Hadyn Fitzgerald
 */
public interface AssetLoader {

    /**
     * The blank asset loader.
     */
    AssetLoader DUMMY = (uri) -> new byte[0];

    /**
     * Loads an asset from a URI.
     *
     * @param uri The unique resource identifier, {@link URI}
     * @return The data for the asset.
     * @throws URISyntaxException Thrown if the provided unique resource identify has invalid syntax.
     * @throws AssetLoaderException If there was an issue with loading the referenced asset.
     */
    default byte[] load(String uri) throws URISyntaxException, AssetLoaderException {
        return load(new URI(uri));
    }

    /**
     * Loads an asset from a URI.
     *
     * @param uri The unique resource identifier.
     * @return The data for the asset.
     */
    byte[] load(URI uri) throws AssetLoaderException;
}