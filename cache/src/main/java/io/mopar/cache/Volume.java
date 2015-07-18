package io.mopar.cache;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public class Volume {

    /**
     * The id.
     */
    private int id;

    /**
     * The blob.
     */
    private Blob blob;

    /**
     * The index.
     */
    private Index index;

    /**
     * Constructs a new {@link Volume};
     *
     * @param id The volume id.
     * @param blob The blob.
     * @param index The index.
     */
    public Volume(int id, Blob blob, Index index) throws IOException {
        this.id = id;
        this.blob = blob;
        this.index = index;
    }

    /**
     * Reads a file fully.
     *
     * @param fileId The file id.
     * @return If the file was successfully read, returns <code>false</code> if the file does not exist.
     * @throws IOException an I/O exception was encountered while reading the file.
     */
    public byte[] readFully(int fileId) throws IOException {
        if(!exists(fileId)) {
            return null;
        }

        byte[] src = new byte[length(fileId)];

        // Read the file, if the file was not successfully read then return null. This will happen if
        // the file is corrupted :(
        if(read(fileId, 0, src, 0, src.length) == -1) {
            return null;
        }

        return src;
    }

    /**
     * Reads a file.
     *
     * @param fileId The file id.
     * @param dest The destination array.
     * @return The amount of read bytes or <code>-1</code> if the file does not exist or is corrupted.
     * @throws IOException
     */
    public int read(int fileId, byte[] dest) throws IOException {
        return read(fileId, 0, dest, 0, dest.length);
    }

    /**
     * Reads a file.
     *
     * @param fileId The file id.
     * @param srcOff The offset in the source data.
     * @param dest The destination array.
     * @param destOff The destination offset.
     * @param len The amount of bytes to read.
     * @return The amount of read bytes or <code>-1</code> if the file does not exist or is corrupted.
     * @throws IOException an I/O exception was encountered while reading the file.
     */
    public int read(int fileId, int srcOff, byte[] dest, int destOff, int len) throws IOException {

        // Check if the file exists in the volume before we continue
        if(!exists(fileId)) {
            return -1;
        }

        Reference reference = index.read(fileId);

        // Check if the source offset is valid
        if(srcOff < 0 || srcOff >= reference.getLength()) {
            throw new ArrayIndexOutOfBoundsException(srcOff);
        }

        // Trim the length if needed
        if(srcOff + len > reference.getLength()) {
            len = reference.getLength() - srcOff;
        }

        Sector sector = new Sector();

        // Read up until the chunk for the source offset
        int sectorId = reference.getSectorId();
        int chunk = 0;
        for(int off = Sector.DATA_LENGTH; off <= srcOff; chunk++) {
            if(!blob.read(sectorId, sector) || sector.getVolumeId() != id || sector.getFileId() != fileId || sector.getChunk() != chunk) {
                return -1;
            }
            sectorId = sector.getNextSector();
            off += Sector.DATA_LENGTH;
        }

        // Read the file data
        for(int off = 0; off < len; chunk++) {
            int pos = srcOff + off;

            int sectorOff = pos % Sector.DATA_LENGTH;
            int read = Sector.DATA_LENGTH - sectorOff;
            if(read > len - off) {
                read = len - off;
            }

            if(!blob.read(sectorId, sector) || sector.getVolumeId() != id || sector.getFileId() != fileId || sector.getChunk() != chunk) {
                return -1;
            }

            System.arraycopy(sector.getBytes(), sectorOff, dest, destOff, read);
            sectorId = sector.getNextSector();
            destOff += read;
            off += read;
        }
        return len;
    }

    /**
     * Writes a file.
     *
     * @param fileId The file id.
     * @param src The file source.
     * @return If the file was written successfully.
     * @throws IOException an I/O exception was encountered while writing the file.
     */
    public boolean write(int fileId, byte[] src) throws IOException {
        return write(fileId, src, 0, src.length);
    }

    /**
     * Writes a file.
     *
     * @param fileId The file id.
     * @param src The file source.
     * @param off The offset in the file.
     * @param len The length of the file.
     * @return If the file was written successfully.
     * @throws IOException an I/O exception was encountered while writing the file.
     */
    public boolean write(int fileId, byte[] src, int off, int len) throws IOException {
        if(!write(fileId, src, off, len, false)) {
            return write(fileId, src, off, len, true);
        }
        return false;
    }

    /**
     * Writes a file.
     *
     * @param fileId The file id.
     * @param src The file source.
     * @param srcOff The offset in the file source.
     * @param len The length of the file.
     * @param append Flag for if the file should be attempted to appended.
     * @return If the file was written successfully.
     * @throws IOException an I/O exception was encountered while writing the file.
     */
    private boolean write(int fileId, byte[] src, int srcOff, int len, boolean append) throws IOException {
        if(srcOff < 0 || srcOff + len > src.length) {
            throw new ArrayIndexOutOfBoundsException(srcOff);
        }

        Reference reference;
        if(append) {
            index.write(reference = new Reference(len, blob.freeSector()), fileId);
        } else {
            if(!index.exists(fileId)) {
                return false;
            }
            reference = index.read(fileId);
        }

        Sector sector = new Sector();
        int sectorId = reference.getSectorId();
        int off = 0;
        for(int chunk = 0; off < len; chunk++) {
            if(sectorId == Sector.EOE) {
                append = true;
                sectorId = blob.freeSector();
            }

            if(!append) {
                if(!blob.exists(sectorId)) {
                    return false;
                }

                blob.read(sectorId, sector);
                if(sector.getVolumeId() != id || sector.getFileId() != fileId || sector.getChunk() != chunk) {
                    return false;
                }
            } else {
                sector.setFileId(fileId);
                sector.setChunk(chunk);
                sector.setNextSector(sectorId + 1);
                sector.setVolumeId(id);
            }

            int read = len - off;
            if(read > Sector.DATA_LENGTH) {
                read = Sector.DATA_LENGTH;
            } else {
                sector.setNextSector(Sector.EOE);
            }

            System.arraycopy(src, srcOff + off, sector.getBytes(), 0, read);
            blob.write(sector, sectorId);

            sectorId = sector.getNextSector();
            off += read;
        }
        return true;
    }

    /**
     * Gets if a file exists in the volume.
     *
     * @param fileId The file id.
     * @return If the file exists.
     */
    public boolean exists(int fileId) {
        return index.exists(fileId);
    }

    /**
     * Gets the length of a file in the volume.
     *
     * @param fileId The file id.
     * @return The length of the file in bytes.
     */
    public int length(int fileId) throws IOException {
        if(!exists(fileId)) {
            return -1;
        }
        Reference reference = index.read(fileId);
        return reference.getLength();
    }
}
