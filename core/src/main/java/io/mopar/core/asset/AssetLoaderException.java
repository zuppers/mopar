package io.mopar.core.asset;

import java.io.IOException;

/**
 * Created by hadyn on 6/24/15.
 */
public class AssetLoaderException extends IOException {
    public AssetLoaderException() {}

    public AssetLoaderException(String message) {
        super(message);
    }

    public AssetLoaderException(String message, Throwable reason) {
        super(message, reason);
    }
}
