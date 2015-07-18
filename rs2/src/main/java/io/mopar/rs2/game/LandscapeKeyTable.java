package io.mopar.rs2.game;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class LandscapeKeyTable {

    /**
     * Empty key set for null keys.
     */
    private static final int[] EMPTY_KEY_SET = new int[4];

    /**
     * The keys.
     */
    private Map<Integer, int[]> keys = new HashMap<>();

    /**
     * Constructs a new {@linl LandscapeKeyTable};
     */
    public LandscapeKeyTable() {}

    /**
     * Parses data for the table.
     *
     * @param data The data to parse.
     */
    public void parse(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        while(buffer.remaining() >= 18) {
            int hash = buffer.getShort() & 0xffff;
            int[] arr = new int[4];
            for(int i = 0; i < arr.length; i++) {
                arr[i] = buffer.getInt();
            }
            keys.put(hash, arr);
        }
    }

    /**
     * Gets the cipher keys for a region.
     *
     * @param regionX The region x.
     * @param regionY The region y.
     * @return the keys for the specific regions.
     */
    public int[] getKeys(int regionX, int regionY) {
        int hash = regionX << 8 | regionY;
        if(!keys.containsKey(hash)) {
            return EMPTY_KEY_SET;
        }
        return keys.get(regionX << 8 | regionY);
    }
}
