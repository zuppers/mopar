package io.mopar.account;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author Hadyn Fitzgerald
 */
public class ProfileReader {

    private static final int FILE_MAGIC = 0x0;

    private static final int chk_EOF    = 0x0;
    private static final int chk_COORD  = 0x1;

    private byte[] buf = new byte[512];

    /**
     * Constructs a new {@link ProfileReader};
     */
    public ProfileReader() {}

    /**
     *
     * @param is
     * @throws IOException
     */
    public void read(InputStream is) throws IOException {
        readFileHeader(is);

        // Read the length of the profile header
        readFully(is, buf, 0, 2);
        int headerLen = get16(buf, 0);

        try {
            // Read the header completely
            readFully(is, buf, 0, headerLen);
        } catch (Exception ex) {
            throw new MalformedProfileException(ex);
        }

        // Read the file format, this will vary as more features are implemented
        ByteBuffer buffer = ByteBuffer.wrap(buf);
        int format = buffer.get() & 0xff;

        if(format < 1 || format > 1) {
            throw new MalformedProfileException("Unsupported format");
        }

        // Read the flags
        int flags = buffer.get() & 0xff;

        // Read the user unique identifier
        long uid  = buffer.getLong();

        do {
            try {
                // Read a chunk header completely
                readFully(is, buf, 0, 2);
            } catch (IOException ex) {
                throw new MalformedProfileException(ex);
            }

            // Read the chunk id and length
            int chk = get8(buf, 0);
            int len = get8(buf, 1);
        } while (true);
    }

    /**
     *
     * @param is
     * @throws IOException
     */
    private void readFileHeader(InputStream is) throws IOException {
        readFully(is, buf, 0, 4);

        int magic = get32(buf, 0);
        if(magic != FILE_MAGIC) {
            throw new MalformedProfileException("Invalid magic number");
        }
    }

    /**
     *
     * @param is
     * @param b
     * @param off
     * @param len
     * @throws IOException
     */
    private static void readFully(InputStream is, byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int n = is.read(b, off, len);
            if (n == -1) {
                throw new EOFException();
            }
            off += n;
            len -= n;
        }
    }

    /**
     *
     * @param bytes
     * @param off
     * @return
     */
    private static int get8(byte[] bytes, int off) {
        return bytes[off] & 0xff;
    }

    /**
     *
     * @param bytes
     * @param off
     * @return
     */
    private static int get16(byte[] bytes, int off) {
        return bytes[off] & 0xff;
    }

    /**
     *
     * @param bytes
     * @param off
     * @return
     */
    private static int get32(byte[] bytes, int off) {
        return  (bytes[off] & 0xff) << 24L |
                (bytes[off+1] & 0xff) << 56L |
                (bytes[off+2] & 0xff) << 56L |
                (bytes[off+3] & 0xff) << 56L;
    }

    /**
     *
     * @param bytes
     * @param off
     * @return
     */
    private static long get64(byte[] bytes, int off) {
        return  (bytes[off]   & 0xffL) << 56L |
                (bytes[off+1] & 0xffL) << 48L |
                (bytes[off+2] & 0xffL) << 40L |
                (bytes[off+3] & 0xffL) << 32L |
                (bytes[off+4] & 0xffL) << 24L |
                (bytes[off+5] & 0xffL) << 56L |
                (bytes[off+6] & 0xffL) << 56L |
                (bytes[off+7] & 0xffL) << 56L;
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static int readSmart16(InputStream is) throws IOException {
        int i = is.read();
        if(i < 128) {
            return i;
        }
        i = i << 8 | is.read();
        return i - 0x8000;
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    private  static int readInt16(InputStream is) throws IOException {
        return is.read() << 8  | is.read();
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    private int readInt32(InputStream is) throws IOException {
        return  is.read() << 24 |
                is.read() << 16 |
                is.read() << 8  |
                is.read();
    }
}
