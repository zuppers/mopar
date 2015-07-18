package io.mopar.cache;

/**
 * Created by hadyn on 6/29/2015.
 */
public class FileSystemConfig {

    /**
     * The amount of sequential sectors to reserve in memory.
     */
    private int cachedSectors;

    /**
     * The amount of sequential references to reserve in memory.
     */
    private int cachedReferences;

    /**
     * Constructs a new {@link FileSystemConfig};
     *
     * @param cachedSectors The number of sectors to buffer into memory.
     * @param cachedReferences The number of references to buffer into memory.
     */
    public FileSystemConfig(int cachedSectors, int cachedReferences) {
        this.cachedSectors = cachedSectors;
        this.cachedReferences = cachedReferences;
    }

    /**
     * Gets the amount of readable and writable memory to dedicate for sectors for the blob.
     *
     * @return The sector memory in bytes.
     */
    public int getSectorMemory() {
        return cachedSectors * Sector.LENGTH;
    }

    /**
     * Gets the amount of readable and writable memory to dedicate for references for an index.
     *
     * @return The reference memory in bytes.
     */
    public int getReferenceMemory() {
        return cachedReferences * Reference.LENGTH;
    }
}
