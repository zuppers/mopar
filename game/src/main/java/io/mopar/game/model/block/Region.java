package io.mopar.game.model.block;

import io.mopar.game.config.GameObjectConfig;
import io.mopar.game.model.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hadyn Fitzgerald
 */
public class Region {

    private static final int chk_EOF    = 0x0;
    private static final int chk_LOCALE = 0x1;
    private static final int chk_CLIP   = 0x2;
    private static final int chk_NPC    = 0x3;

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
     * The chunks.
     */
    private Block[][][] blocks = new Block[4][8][8];

    /**
     * The traversal maps.
     */
    private TraversalMap[] traversalMaps = new TraversalMap[4];

    /**
     * The touched time.
     */
    private long lastTouchedTime;

    /**
     * The updated chunks.
     */
    private Set<Block> updatedBlocks = new HashSet<>();

    /**
     * Flag for if the region was updated.
     */
    private boolean updated;

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
                    int config = dis.readUnsignedShort();

                    int loc = dis.readUnsignedShort();
                    int x = loc >> 6 & 0x3f;
                    int y = loc & 0x3f;
                    int plane = loc >> 12;

                    int ori = dis.readUnsignedByte();
                    int type = ori >> 2;
                    int orientation = ori & 0x3;

                    int group = GameObjectGroup.getGroupForType(type);

                    int chunkX = x >> 3,  chunkY = y >> 3;
                    int localX = x & 0x7, localY = y & 0x7;

                    Block block = getBlock(plane, chunkX, chunkY);
                    BlockTile tile = block.getTile(localX, localY);

                    GameObject gameObject = new GameObject(config, type, orientation, true);
                    tile.setLocale(group, gameObject);
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

                case chk_NPC: {
                    int type = dis.readUnsignedShort();

                    int loc = dis.readUnsignedByte();
                    int x = loc >> 6 & 0x3f;
                    int y = loc & 0x3f;
                    int plane = loc >> 12;
                }
                break;

                default:
                    throw new IOException("Bad config code");
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param length
     */
    public void markOccupant(int x, int y, int width, int length, int orientation) {

    }

    /**
     * Gets the chunk at the provided coordinates.
     *
     * @param x the local chunk x coordinate.
     * @param y the local chunk y coordinate.
     * @return the chunk for the coordinates.
     */
    public Block getBlock(int plane, int x, int y) {
        Block block = blocks[plane][x][y];
        if(block == null) {
            block = blocks[plane][x][y] = new Block(this, plane, x, y);
        }
        return block;
    }

    /**
     *
     * @param x
     * @param y
     * @param plane
     * @return
     */
    public boolean isChunkUpdated(int x, int y, int plane) {
        if(blocks[plane][x][y] == null) {
            return false;
        }
        Block block = blocks[plane][x][y];
        return block.isUpdated();
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

    public void addUpdated(Block block) {
        updatedBlocks.add(block);
    }

    /**
     *
     * @return
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Resets the region.
     */
    public void reset() {
        for(Block block : updatedBlocks) {
            block.reset();
        }

        updatedBlocks.clear();
        updated = false;
    }

    public Set<Block> getUpdatedBlocks() {
        return updatedBlocks;
    }

    /**
     *
     * @param plane
     * @param x
     * @param y
     * @param type
     * @param configId
     * @param orientation
     */
    public void markObject(int plane, int x, int y, int type, int configId, int orientation) {
        int group = GameObjectGroup.getGroupForType(type);
        GameObjectConfig config = GameObjectConfig.forId(configId);
        TraversalMap map = traversalMaps[plane];

        if(group == GameObjectGroup.WALL) {
            if(config.isSolid()) {
                map.markWall(x, y, type, orientation, config.isImpenetrable());
            }
        }

        if(group == GameObjectGroup.GROUP_2) {
            if(config.isSolid()) {
                map.markOccupant(x, y, config.getWidth(), config.getHeight(), config.isImpenetrable());
            }
        }
    }

    /**
     *
     * @param plane
     * @param x
     * @param y
     * @param type
     * @param configId
     * @param orientation
     */
    public void unmarkObject(int plane, int x, int y, int type, int configId, int orientation) {
        int group = GameObjectGroup.getGroupForType(type);
        GameObjectConfig config = GameObjectConfig.forId(configId);
        TraversalMap map = traversalMaps[plane];

        if(group == GameObjectGroup.WALL) {
            if(config.isSolid()) {
                map.unmarkWall(x, y, type, orientation, config.isImpenetrable());
            }
        }

        if(group == GameObjectGroup.GROUP_2) {
            if(config.isSolid()) {
                map.unmarkOccupant(x, y, config.getWidth(), config.getHeight(), config.isImpenetrable());
            }
        }
    }
}
