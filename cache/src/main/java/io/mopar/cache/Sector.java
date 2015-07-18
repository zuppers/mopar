package io.mopar.cache;

import io.mopar.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * Created by hadyn on 6/29/2015.
 */
public class Sector {

    /**
     * The length of a sector.
     */
    public static final int LENGTH = 520;

    /**
     * The amount of bytes of data stored.
     */
    public static final int DATA_LENGTH = 512;

    /**
     * The end of entry sector id.
     */
    public static final int EOE = 0;

    /**
     * The file id.
     */
    private int fileId;

    /**
     * The volume id.
     */
    private int volumeId;

    /**
     * The next sector.
     */
    private int nextSector;

    /**
     * The chunk.
     */
    private int chunk;

    /**
     * The data.
     */
    private byte[] bytes = new byte[DATA_LENGTH];

    /**
     * Constructs a new {@link Sector};
     */
    public Sector() {}

    /**
     * Constructs a new {@link Sector};
     *
     * @param fileId The file id.
     * @param chunk The chunk.
     * @param nextSector The next sector.
     * @param volumeId The volume id.
     */
    public Sector(int fileId, int chunk, int nextSector, int volumeId) {
        this.fileId = fileId;
        this.chunk = chunk;
        this.nextSector = nextSector;
        this.volumeId = volumeId;
    }

    /**
     * Decodes a sector.
     *
     * @param buffer The buffer to decode the sector from.
     */
    public void decode(ByteBuffer buffer) {
        fileId = buffer.getShort() & 0xffff;
        chunk = buffer.getShort() & 0xffff;
        nextSector = ByteBufferUtil.getUnsignedMedium(buffer);
        volumeId = buffer.get() & 0xff;
        buffer.get(bytes);
    }

    /**
     * Encodes a sector.
     *
     * @param buffer The buffer to encode the sector to.
     */
    public void encode(ByteBuffer buffer) {
        buffer.putShort((short) fileId);
        buffer.putShort((short) chunk);
        ByteBufferUtil.putMedium(buffer, nextSector);
        buffer.put((byte) volumeId);
        buffer.put(bytes);
    }

    /**
     * Sets the file id.
     *
     * @param fileId The file id.
     */
    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    /**
     * Gets the file id.
     *
     * @return The file id.
     */
    public int getFileId() {
        return fileId;
    }

    /**
     * Sets the chunk.
     *
     * @param chunk The chunk.
     */
    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    /**
     * Gets the chunk id.
     *
     * @return The chunk.
     */
    public int getChunk() {
        return chunk;
    }

    /**
     * Sets the volume id.
     *
     * @param volumeId The volume id.
     */
    public void setVolumeId(int volumeId) {
        this.volumeId = volumeId;
    }

    /**
     * Gets the volume id.
     *
     * @return The volume id.
     */
    public int getVolumeId() {
        return volumeId;
    }

    /**
     * Sets the next sector.
     *
     * @param nextSector The next sector.
     */
    public void setNextSector(int nextSector) {
        this.nextSector = nextSector;
    }

    /**
     * Gets the next sector id.
     *
     * @return The next sector.
     */
    public int getNextSector() {
        return nextSector;
    }

    /**
     * Gets the data.
     *
     * @return The data.
     */
    public byte[] getBytes() {
        return bytes;
    }
}