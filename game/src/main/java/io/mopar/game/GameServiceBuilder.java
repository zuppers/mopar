package io.mopar.game;

import io.mopar.account.ProfileSerializer;
import io.mopar.core.asset.AssetLoader;

/**
 * @author Hadyn Fitzgerald
 */
public class GameServiceBuilder {

    /**
     * The service.
     */
    private GameService service = new GameService();

    /**
     *
     * @param serializer
     * @return
     */
    public GameServiceBuilder profileSerialize(ProfileSerializer serializer) {
        service.setProfileSerializer(serializer);
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
