package io.mopar.cache;

import io.mopar.util.BufferedFile;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Hadyn Fitzgerald
 */
public class Blob {

    /**
     * The byte buffer used for reading and writing sectors.
     */
    private byte[] bytes = new byte[Sector.LENGTH];

    /**
     * The file.
     */
    private BufferedFile file;

    /**
     * Constructs a new {@link Blob};
     *
     * @param file The blob file.
     */
    public Blob(BufferedFile file) {
        this.file = file;
    }

    /**
     * Reads a sector.
     *
     * @param sectorId The sector.
     * @param sector The sector to read the data to.
     * @return If the sector was successfully read, returns <code>false</code> if the sector does not exist.
     * @throws IOException an I/O exception was encountered while reading the sector.
     */
    public boolean read(int sectorId, Sector sector) throws IOException {
        if(!exists(sectorId)) {
            return false;
        }
        seek(sectorId);

        file.read(bytes);
        sector.decode(ByteBuffer.wrap(bytes));
        return true;
    }

    /**
     * Reads a sector.
     *
     * @param sectorId The sector id.
     * @return The sector or <code>null</code> if the blob does not contain a sector at the specified id.
     */
    public Sector read(int sectorId) throws IOException {
        if(!exists(sectorId)) {
            return null;
        }
        seek(sectorId);

        file.read(bytes);

        Sector sector = new Sector();
        sector.decode(ByteBuffer.wrap(bytes));
        return sector;
    }

    /**
     * Writes a sector.
     *
     * @param sector The sector to write.
     * @param sectorId The sector id.
     * @throws IOException
     */
    public void write(Sector sector, int sectorId) throws IOException {
        seek(sectorId);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        sector.encode(buffer);

        file.write(bytes);
    }

    /**
     * Seeks to a sector.
     *
     * @param sid The sector id.
     * @throws IOException an I/O exception was encountered while seeking to the specified sector.
     */
    public void seek(int sid) throws IOException {
        file.seek(Sector.LENGTH * sid);
    }

    /**
     * Gets if the blob contains a sector.
     *
     * @param sid The sector id.
     * @return If the sector exists in the blob.
     */
    public boolean exists(int sid) {
        return length() > sid;
    }

    /**
     * Gets the amount of sectors contained in the blob.
     *
     * @return The amount of sectors.
     */
    public int length() {
        return (int) ((file.length() + Sector.LENGTH - 1L) / (long) Sector.LENGTH);
    }

    /**
     *
     * @return
     */
    public boolean empty() {
        return length() == 0;
    }

    /**
     * Gets the next free sector.
     *
     * @return The next free sector.
     */
    public int freeSector() {
        return empty() ? 1 : length();
    }
}
