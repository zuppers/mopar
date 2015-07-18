package io.mopar.file;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.mopar.cache.ContainerHeader;
import io.mopar.cache.FileSystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

/**
 * @author Hadyn Fitzgerald
 */
public class DiskFileProvider implements FileProvider {

    /**
     * The file system.
     */
    private FileSystem fileSystem;

    /**
     * The file cache.
     */
    private LoadingCache<Integer, byte[]> files;

    /**
     * Constructs a new {@link DiskFileProvider};
     *
     * @param fileSystem The file system.
     */
    public DiskFileProvider(FileSystem fileSystem) {
        this(fileSystem, 500);
    }

    /**
     * Constructs a new {@link DiskFileProvider};
     *
     * @param fileSystem The file system.
     * @param cachedFiles The number of cached files.
     */
    public DiskFileProvider(FileSystem fileSystem, int cachedFiles) {
        this.fileSystem = fileSystem;

        files = CacheBuilder.newBuilder()
                .maximumSize(cachedFiles)
                .build(new CacheLoader<Integer, byte[]>() {
                    @Override
                    public byte[] load(Integer key) throws Exception {
                        int volumeId = (key >> 16) & 0xff;
                        int fileId = key & 0xffff;
                        return fileSystem.readFully(volumeId, fileId);
                    }
                });
    }

    /**
     * Reads data for a file.
     *
     * @param volumeId The volume id.
     * @param fileId The file id.
     * @param srcOff The offset in the source.
     * @param dest The destination buffer.
     * @param destOff The offset in the destination buffer.
     * @param len The amount of bytes to read.
     * @return The amount of read bytes.
     */
    @Override
    public int read(int volumeId, int fileId, int srcOff, byte[] dest, int destOff, int len) {
        try {
            byte[] src = files.get(volumeId << 16 | fileId);
            if(len + srcOff > src.length) {
                len = src.length - srcOff;
            }
            System.arraycopy(src, srcOff, dest, destOff, len);
            return len;
        } catch (ExecutionException ex) {
            return -1;
        }
    }

    @Override
    public FileChunk getChunk(int volumeId, int fileId, int id, boolean priority) throws IOException {
        ContainerHeader header = fileSystem.readHeader(volumeId, fileId);

        // Calculate the offset in the source, first data block is either 504 bytes or 500 bytes long. We skip over the
        // container header and just write the packed bytes, from there each block is 511 bytes long.
        int off = (id > 0 ? (FileChunk.LENGTH - (header.isCompressed() ? 12 : 8)) + ((id - 1) * FileChunk.DATA_LENGTH) : 0) + header.length();

        // Get the amount of bytes to read for the chunk, not taking into account the header
        ByteBuffer buffer = ByteBuffer.allocate(FileChunk.LENGTH);

        if(id == 0) {
            // Pack the header
            buffer.put((byte) volumeId);
            buffer.putShort((short) fileId);
            buffer.put((byte) (header.getCompression() | (priority ? 0 : 0x80)));
            buffer.putInt(header.getPackedLength());

            // If the container is compressed, then write the unpacked length
            if(header.isCompressed()) {
                buffer.putInt(header.getUnpackedLength());
            }
        } else {

            // No header
            buffer.put((byte) -1);
        }

        int read = header.getPackedLength() - (off - header.length());
        if(read + buffer.position() >= FileChunk.LENGTH) {
            read = FileChunk.LENGTH - buffer.position();
        }

        read(volumeId, fileId, off, buffer.array(), buffer.position(), read);

        return new FileChunk(buffer.array(), buffer.position() + read);
    }

    @Override
    public boolean exists(int volumeId, int fileId) {
        return fileSystem.exists(volumeId, fileId);
    }

    @Override
    public int chunkedLength(int volumeId, int fileId) throws IOException {
        ContainerHeader header = fileSystem.readHeader(volumeId, fileId);
        int chunks = 1;
        int len = header.getPackedLength() - ((FileChunk.LENGTH - (header.isCompressed() ? 12 : 8)));
        if(len > 0) {
            chunks += (len + FileChunk.DATA_LENGTH - 1) / FileChunk.DATA_LENGTH;
        }
        return chunks;
    }
}
