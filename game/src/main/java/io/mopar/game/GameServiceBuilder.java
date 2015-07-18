package io.mopar.game;

import io.mopar.core.asset.AssetLoader;
import io.mopar.core.profile.ProfileCodec;

/**
 * @author Hadyn Fitzgerald
 */
public class GameServiceBuilder {

    /**
     *
     */
    private GameService service = new GameService();

    /**
     *
     * @param profileCodec
     * @return
     */
    public GameServiceBuilder profileCodec(ProfileCodec profileCodec) {
        service.setProfileCodec(profileCodec);
        return this;
    }

    /**
     *
     * @param assetLoader
     * @return
     */
    public GameServiceBuilder assetLoader(AssetLoader assetLoader) {
        service.setAssetLoader(assetLoader);
        return this;
    }

    /**
     *
     * @return
     */
    public GameService build() {
        return service;
    }

    /**
     *
     * @return
     */
    public static GameServiceBuilder create() {
        return new GameServiceBuilder();
    }
}
