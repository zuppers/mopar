package io.mopar.cache;

import io.mopar.cache.bzip.HBZip2InputStream;
import io.mopar.cache.bzip.HBZip2OutputStream;
import io.mopar.util.ByteBufferUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Hadyn Fitzgerald
 */
public class Container {

    public static final int NONE = 0;
    public static final int BZIP2 = 1;
    public static final int GZIP = 2;

    /**
     * The CRC32 instance.
     */
    private static final CRC32 crc = new CRC32();

    /**
     * The compression.
     */
    private int compression;

    /**
     * The revision.
     */
    private int revision;

    /**
     * The bytes.
     */
    private byte[] bytes;

    /**
     * Constructs a new {@link Container};
     */
    public Container() {}

    /**
     * Sets the compression.
     *
     * @param compression The compression.
     */
    public void setCompression(int compression) {
        this.compression = compression;
    }

    /**
     * Sets the revision.
     *
     * @param revision The revision.
     */
    public void setRevision(int revision) {
        this.revision = revision;
    }

    /**
     * Gets the revision.
     *
     * @return The revision.
     */
    public int getRevision() {
        return revision;
    }

    /**
     * Sets the container data.
     *
     * @param bytes The data.
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Gets the container data.
     *
     * @return The data.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Gets the amount of bytes of data.
     *
     * @return The length of the container.
     */
    public int length() {
        return bytes.length;
    }

    /**
     * Gets the checksum for the data.
     *
     * @return The CRC32 checksum value.
     */
    public int getCrc32() {
        crc.reset();
        crc.update(bytes);
        return (int) crc.getValue();
    }

    /**
     * Packs the container.
     *
     * @return The packed container data.
     * @throws IOException An I/O exception was caught.
     */
    public byte[] pack() throws IOException {

        // Get the packed container bytes and calculate
        // the length of the container header
        int headerLength = 5;
        byte[] packed;
        if(compression != NONE) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // Pack the container data using the specified compression algorithm
            if(compression == BZIP2) {
                GZIPOutputStream os = new GZIPOutputStream(bos);
                os.write(bytes, 0, bytes.length);
            } else if(compression == GZIP) {
                HBZip2OutputStream os = new HBZip2OutputStream(bos);
                os.write(bytes, 0, bytes.length);
            } else {
                throw new IOException("Unsupported compression");
            }

            packed = bos.toByteArray();
            headerLength += 4;
        } else {
            packed = bytes;
        }

        boolean versioned = revision != -1;

        // Pack the container, write the container, unpacked length, if the container is
        // compressed; the packed length, and if the container is versioned the revision.
        ByteBuffer buffer = ByteBuffer.allocate(headerLength + packed.length + (versioned ? 2 : 0));
        buffer.put((byte) compression);
        buffer.putInt(bytes.length);
        if(compression != NONE) {
            buffer.putInt(packed.length);;
        }
        buffer.put(packed);

        // If the container revision attribute is set then write out the
        // entry revision
        if(versioned) {
            buffer.putShort((short) revision);
        }

        return buffer.array();
    }

    /**
     * Unpacks data to a container.
     *
     * @param bytes The bytes.
     * @return The created container.
     * @throws IOException an I/O exception was encountered while unpacking the container.
     */
    public static Container unpack(byte[] bytes) throws IOException {
        return unpack(bytes, 0, bytes.length);
    }

    /**
     * Unpacks data to a container.
     *
     * @param bytes The bytes.
     * @param off The offset in the bytes.
     * @param len The amount of bytes.
     * @return The created container.
     * @throws IOException an I/O exception was encountered while unpacking the container.
     */
    public static Container unpack(byte[] bytes, int off, int len) throws IOException {
        // Parse the container header and get the compression type and packed length
        ByteBuffer buffer = ByteBuffer.wrap(bytes, off, len);

        int type = buffer.get() & 0xff;
        int packedLength = buffer.getInt();

        // Create the container and set the container's compression type
        Container container = new Container();
        container.setCompression(type);

        // Decompress the container if required and set the container's bytes
        if(type != NONE) {
            int unpackedLength = buffer.getInt();
            byte[] unpacked = new byte[unpackedLength];

            // Skip over the packed bytes in the container
            ByteBufferUtil.skip(buffer, packedLength);

            // Unpack the entry based upon its compression type
            InputStream is;
            if(type == GZIP) {
                is = new GZIPInputStream(new ByteArrayInputStream(bytes, 9, packedLength));
            } else if(type == BZIP2) {
                is = new HBZip2InputStream(new ByteArrayInputStream(bytes, 9, packedLength));
            } else {
                throw new IOException("Unhandled compression type");
            }

            int offset = 0;
            while(offset < unpackedLength) {
                int read = is.read(unpacked, offset, unpackedLength - offset);
                if(read == -1) {
                    break;
                }
                offset += read;
            }

            container.setBytes(unpacked);
        } else {
            byte[] unpacked = new byte[packedLength];
            buffer.get(unpacked, 0, packedLength);
            container.setBytes(unpacked);
        }

        // If the version is encoded at the footer of the container
        // read it from the buffer and set the container version
        // attribute
        boolean versionEncoded = buffer.remaining() >= 2;
        if(versionEncoded) {
            container.setRevision(buffer.getShort() & 0xffff);
        }

        return container;
    }
}
