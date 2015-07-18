package io.mopar.util;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Hadyn Fitzgerald
 */
public class FileOnDisk {

    public static final long UNSPECIFIED_MAXIMUM_LENGTH = -1;

    private long maxLength;
    private long position;
    private File file;
    private RandomAccessFile randomAccessFile;

    public FileOnDisk(Path path, String access) throws IOException {
        this(path.toFile(), access);
    }

    public FileOnDisk(File file, String access) throws IOException {
        this(file, access, UNSPECIFIED_MAXIMUM_LENGTH);
    }

    public FileOnDisk(File file, String access, long maxLength) throws IOException {
        if(maxLength == UNSPECIFIED_MAXIMUM_LENGTH) {
            maxLength = Long.MAX_VALUE;
        }

        // If the specified file on disk is longer than the maximum allowed
        // size then just delete the file
        if(file.length() >= maxLength) {
            file.delete();
        }

        randomAccessFile = new RandomAccessFile(file, access);
        this.file = file;
        this.maxLength = maxLength;
        position = 0L;
        int read = randomAccessFile.read();
        if(read != -1 && !access.equals("r")) {
            randomAccessFile.seek(0L);
            randomAccessFile.write(read);
        }
        randomAccessFile.seek(0L);
    }

    public void seek(long position) throws IOException {
        randomAccessFile.seek(position);
        this.position = position;
    }

    public int read(byte[] dest) throws IOException {
        return read(dest, 0, dest.length);
    }

    public int read(byte[] dest, int off, int len) throws IOException {
        int read = randomAccessFile.read(dest, off, len);
        if(read > 0) {
            position += (long) read;
        }
        return read;
    }

    public void write(byte[] src) throws IOException {
        write(src, 0, src.length);
    }

    public void write(byte[] src, int off, int len) throws IOException {
        if(maxLength < position + len) {
            randomAccessFile.seek(len + 1L);
            randomAccessFile.write(1);
            throw new EOFException();
        }
        randomAccessFile.write(src, off, len);
        position += (long) len;
    }

    public long length() throws IOException {
        return randomAccessFile.length();
    }

    public File getFile() {
        return file;
    }

    public void close() throws IOException {
        if (randomAccessFile != null) {
            randomAccessFile.close();
            randomAccessFile = null;
        }
    }

    @Override
    public void finalize() throws Throwable {
        if (randomAccessFile != null) {
            System.out.println("Warning! file on disk " + file + " not closed correctly using close(). Auto-closing instead. ");
            close();
        }
    }
}
