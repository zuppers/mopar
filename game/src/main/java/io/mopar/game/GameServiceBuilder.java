package io.mopar.game;

import io.mopar.core.asset.AssetLoader;
import io.mopar.core.profile.ProfileCodec;

/**
 * @author Hadyn Fitzgerald
 */
public class GameServiceBuilder {

    /**
     * The service.
     */
    private GameService service = new GameService();

    /**
     * Sets the profile codec.
     *
     * @param profileCodec
     * @return
     */
    public GameServiceBuilder profileCodec(ProfileCodec profileCodec) {
        service.setProfileCodec(profileCodec);
        return this;
    }

    /**
     * Sets the asset loader.
     *
     * @param assetLoader
     * @return
     */
    public GameServiceBuilder assetLoader(AssetLoader assetLoader) {
        service.setAssetLoader(assetLoader);
        return this;
    }

    /**
     * Builds the service.
     *
     * @return the built service.
     */
    public GameService build() {
        return service;
    }

    /**
     * Creates a new builder.
     *
     * @return the created builder.
     */
    public static GameServiceBuilder create() {
        return new GameServiceBuilder();
    }
}
