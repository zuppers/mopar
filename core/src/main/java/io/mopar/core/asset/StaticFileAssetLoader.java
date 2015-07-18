package io.mopar.core.asset;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Hadyn Fitzgerald
 */
public class StaticFileAssetLoader implements AssetLoader {

    private Path path;

    public StaticFileAssetLoader(String path) {
        this(Paths.get(path));
    }

    public StaticFileAssetLoader(Path path) {
        this.path = path;
    }

    @Override
    public byte[] load(URI uri) throws AssetLoaderException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try(InputStream fis = Files.newInputStream(path.resolve(uri.getPath()), StandardOpenOption.READ)) {
            byte[] buffer = new byte[1024];
            int read;
            while((read = fis.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            throw new AssetLoaderException("Failed to load asset " + uri, ex);
        }

        return os.toByteArray();
    }
}
