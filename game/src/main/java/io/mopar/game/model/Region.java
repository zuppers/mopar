package io.mopar.game.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Hadyn Fitzgerald
 */
public class Region {

    private static final int chk_EOF    = 0x0;
    private static final int chk_LOCALE = 0x1;
    private static final int chk_CLIP   = 0x2;

    private static final int clip_EOD   = 0xffff;

    /**
     * The x coordinate.
     */
    private int x;

    /**
     * The y coordinate.
     */
    private int y;

    /**
     * The traversal maps.
     */
    private TraversalMap[] traversalMaps = new TraversalMap[4];

    /**
     * The touched time.
     */
    private long lastTouchedTime;

    /**
     * Constructs a new {@link Region};
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public Region(int x, int y) {
        for(int i = 0; i < 4; i++) {
            traversalMaps[i] = new TraversalMap(64, 64);
        }
        this.x = x;
        this.y = y;
        touch();
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param is
     * @throws IOException
     */
    public void parse(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);

        if(dis.readInt() != 0x4d52474e) {
            throw new IOException("Invalid file magic");
        }

        int format = dis.readByte();
        if(format > 1 || format < 1) {
            throw new IOException("Unsupported format");
        }

        int locHash = dis.readUnsignedShort();
        int regionX = locHash >> 8;
        int regionY = locHash & 0xff;
        if(x != regionX || y != regionY) {
            throw new IOException("Location mismatch");
        }

        int chk;
        while((chk = dis.readUnsignedByte()) != chk_EOF) {
            switch (chk) {
                case chk_LOCALE: {
                    // 16
                    // 6 + 6 + 2
                    // 10
                    int type    = dis.readUnsignedShort();
                    int loc     = dis.readUnsignedShort();
                    int ori     = dis.readUnsignedByte();
                }
                break;

                case chk_CLIP: {
                    int lhash;
                    while((lhash = dis.readUnsignedShort()) != clip_EOD) {
                        int x = lhash >> 8 & 0x3f;
                        int y = lhash >> 2 & 0x3f;
                        int p = lhash & 0x3;

                        int first = dis.readUnsignedByte();
                        int value = first & 0x3f;

                        int length = first >> 6 & 0x3;
                        if(length >= 2) {
                            value |= dis.readUnsignedByte() << 6;
                        }

                        if(length >= 3) {
                            value |= dis.readUnsignedByte() << 14;
                        }

                        TraversalMap traversalMap = traversalMaps[p];
                        traversalMap.set(x, y, value);
                    }
                }
                break;

                default:
                    throw new IOException("Bad config code");
            }
        }
    }

    /**
     * Touches the region.
     */
    public void touch() {
        lastTouchedTime = System.currentTimeMillis();
    }

    /**
     * Gets the last touched time.
     *
     * @return the last touched time.
     */
    public long getLastTouchedTime() {
        return lastTouchedTime;
    }

    /**
     *
     * @param plane
     * @return
     */
    public TraversalMap getTraversalMap(int plane) {
        return traversalMaps[plane];
    }
}
