package io.mopar.file;

/**
 * @author Hadyn Fitzgerald
 */
public class FileServiceBuilder {

    /**
     * The file service.
     */
    private FileService service = new FileService();

    /**
     * Sets the file provider.
     *
     * @param provider The file provider.
     * @return This builder instance for chaining.
     */
    public FileServiceBuilder provider(FileProvider provider) {
        service.setFileProvider(provider);
        return this;
    }

    /**
     * Builds the service.
     *
     * @return The built service.
     */
    public FileService build() {
        return service;
    }

    /**
     * Creates a new builder.
     *
     * @return The created builder.
     */
    public static FileServiceBuilder create() {
        return new FileServiceBuilder();
    }
}
