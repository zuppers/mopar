package io.mopar.cache;

import io.mopar.util.BufferedFile;
import io.mopar.util.ByteBufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * @author Hadyn Fitzgerald
 */
public class FileSystem {

    /**
     * The default file system configuration.
     */
    public static final FileSystemConfig DEFAULT_CONFIG = new FileSystemConfig(10, 1000);

    /**
     * The file system meta file id.
     */
    public static final int FS_META_FILE_ID = 255;

    /**
     * The root path.
     */
    private Path root;

    /**
     * The amount of volumes.
     */
    private int amountVolumes;

    /**
     * The config.
     */
    private FileSystemConfig config;

    /**
     * The blob file.
     */
    private Blob blob;

    /**
     * The volumes.
     */
    private Volume[] volumes;

    /**
     * The meta information volume.
     */
    private Volume metaVolume;

    /**
     * The volume meta tables.
     */
    private MetaTable[] tables;

    /**
     * The container header buffer.
     */
    private byte[] headerBuffer = new byte[9];

    /**
     * Constructs a new {@link FileSystem};
     *
     * @param root
     * @param amountVolumes
     * @param config
     * @throws IOException
     */
    private FileSystem(Path root, int amountVolumes, FileSystemConfig config) throws IOException {
        if(!Files.isDirectory(root)) {
            throw new IOException("Expected root to be a directory");
        }
        this.root = root;
        this.amountVolumes = amountVolumes;
        this.config = config;

        volumes = new Volume[amountVolumes];

        tables = new MetaTable[amountVolumes];
        for(int i = 0; i < amountVolumes; i++) {
            tables[i] = new MetaTable();
        }

        // Initialize the blob file
        blob = new Blob(new BufferedFile(root.resolve("main_file_cache.dat2"), "rw", config.getSectorMemory()));

        // Initialize the meta volume
        Index metaIndex = new Index(new BufferedFile(root.resolve("main_file_cache.idx" + CacheVolumes.META), "rw", config.getReferenceMemory()));
        metaVolume = new Volume(CacheVolumes.META, blob, metaIndex);

        // Initialize all of the volumes
        for(int i = 0; i < amountVolumes; i++) {
            Index index = new Index(new BufferedFile(root.resolve("main_file_cache.idx" + i), "rw", config.getReferenceMemory()));
            volumes[i] = new Volume(i, blob, index);

            // If a file information table exists for a volume, decode it
            if(metaIndex.exists(i)) {
                Container container = Container.unpack(metaVolume.readFully(i));
                tables[i].decode(container.getBytes());
            }
        }

        // Rebuild the update table
        rebuildMetaTable();
    }

    /**
     * Gets if a file exists.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @return If the file exists.
     */
    public boolean exists(int volumeId, int fileId) {
        return get(volumeId).exists(fileId);
    }

    /**
     * Reads a sector.
     *
     * @param sectorId The sector id.
     * @return The read sector or <code>null</code> if the specified sector does not exist.
     * @throws IOException an I/O exception was caught while reading the sector.
     */
    public Sector readSector(int sectorId) throws IOException {
        return blob.read(sectorId);
    }

    /**
     * Reads the header for a container.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @return The container header.
     * @throws IOException an I/O exception was caught while reading the header for a container.
     */
    public ContainerHeader readHeader(int volumeId, int fileId) throws IOException {
        get(volumeId).read(fileId, headerBuffer);
        ContainerHeader header = new ContainerHeader();
        header.decode(ByteBuffer.wrap(headerBuffer));
        return header;
    }

    /**
     * Reads a file.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @return The file data.
     * @throws IOException
     */
    public byte[] readFully(int volumeId, int fileId) throws IOException {
        return get(volumeId).readFully(fileId);
    }

    /**
     * Gets a volume.
     *
     * @param volumeId The volume id.
     * @return The volume.
     */
    public Volume get(int volumeId) {
        if(volumeId != CacheVolumes.META && (volumeId < 0 || volumeId >= volumes.length)) {
            throw new ArrayIndexOutOfBoundsException(volumeId);
        }
        return volumeId == CacheVolumes.META ? metaVolume : volumes[volumeId];
    }

    /**
     * Gets a volume meta table.
     *
     * @param volumeId The volume id.
     * @return The meta table for the volume.
     */
    public MetaTable getTable(int volumeId) {
        if(volumeId < 0 || volumeId >= volumes.length) {
            throw new ArrayIndexOutOfBoundsException(volumeId);
        }
        return tables[volumeId];
    }

    /**
     * Rebuilds the file system meta table.
     *
     * @throws IOException an I/O exception was encountered while rebuilding the update table.
     */
    public void rebuildMetaTable() throws IOException {
        byte[] bytes = new byte[amountVolumes * 8];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        CRC32 crc = new CRC32();

        for(int i = 0; i < amountVolumes; i++) {
            MetaTable table = tables[i];
            if(metaVolume.exists(i)) {
                byte[] data = metaVolume.readFully(i);
                crc.reset();
                crc.update(data);
                buffer.putInt((int) crc.getValue());
                buffer.putInt(table.getRevision());
            } else {
                ByteBufferUtil.skip(buffer, 8);
            }
        }

        Container container = new Container();
        container.setCompression(Container.NONE);
        container.setBytes(bytes);

        metaVolume.write(FS_META_FILE_ID, container.pack());
    }

    /**
     * Creates a new file system from the given path. Uses the {@link FileSystem#DEFAULT_CONFIG} for configuration.
     *
     * @param path The file system path.
     * @param amountVolumes The amount of volumes.
     * @return The created file system.
     * @throws IOException an I/O exception was encountered while creating the file system.
     */
    public static FileSystem create(Path path, int amountVolumes) throws IOException {
        return create(path, amountVolumes, DEFAULT_CONFIG);
    }

    /**
     * Creates a new file system from the given path.
     *
     * @param path The file system path.
     * @param amountVolumes The amount of volumes.
     * @param config The file system config.
     * @return The created file system.
     * @throws IOException an I/O exception was encountered while creating the file system.
     */
    public static FileSystem create(Path path, int amountVolumes, FileSystemConfig config) throws IOException {
        return new FileSystem(path, amountVolumes, config);
    }
}
