package io.mopar.game.model;

import io.mopar.game.model.block.RegionSet;

import java.util.*;

/**
 * @author Hadyn Fitzgerald
 */
public class BlockSceneGraph {

    public static final int IDLE    = 0;
    public static final int ADDED   = 1;
    public static final int UPDATED = 2;
    public static final int REBUILD = 3;

    /**
     * Number of bits per a state.
     */
    private static final int BITS_PER_STATE = 2;

    /**
     *
     */
    private static final int LENGTH = 13;

    public static class Node {
        int plane, x, y;
        int blockX, blockY;

        Node(int plane, int x, int y, int blockX, int blockY) {
            this.plane = plane;
            this.blockX = blockX;
            this.blockY = blockY;
            this.x = x;
            this.y = y;
        }

        public int getPlane()  { return plane; }
        public int getX() { return x; }
        public int getY() { return y; }

        public int getBlockX() { return blockX; }
        public int getBlockY() { return blockY; }
    }

    /**
     *
     */
    private Set<Node> nodes = new LinkedHashSet<>();

    /**
     *
     */
    private Set<Integer> activeBlocks = new HashSet<>();

    /**
     *
     */
    private BitSet states = new BitSet(LENGTH * LENGTH * BITS_PER_STATE);

    /**
     *
     * @param regions
     * @param currentPosition
     * @param viewDistance
     */
    public void update(RegionSet regions, Position scanPosition, Position currentPosition, int viewDistance, int scanDistance) {
        populate(regions, scanPosition, currentPosition, viewDistance, scanDistance);
        update(regions, currentPosition, viewDistance);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    /**
     *
     * @return
     */
    public Set<Node> getNodes() {
        return nodes;
    }

    /**
     *
     * @param regions
     * @param scanPosition
     * @param position
     * @param viewDistance
     * @param scanDistance
     */
    private void populate(RegionSet regions, Position scanPosition, Position position, int viewDistance, int scanDistance) {
        int span = viewDistance >> 3;
        for (int offsetX = -span; offsetX <= span; offsetX++) {
            for (int offsetY = -span; offsetY <= span; offsetY++) {
                int blockX = position.getBlockX() + offsetX;
                int blockY = position.getBlockY() + offsetY;
                int plane = position.getPlane();

                Position compare = new Position(blockX << 3, blockY << 3, plane);
                if(!position.within(compare, viewDistance)) {
                    continue;
                }

                int blockHash = plane << 24 | blockX << 12 | blockY;

                if(activeBlocks.contains(blockHash)) {
                    continue;
                }

                int regionX = blockX >> 3;
                int regionY = blockY >> 3;

                if(!regions.isLoaded(regionX, regionY)) {
                    continue;
                }

                int x = blockX - (scanPosition.getBlockX() - (scanDistance >> 3));
                int y = blockY - (scanPosition.getBlockY() - (scanDistance >> 3));

                nodes.add(new Node(plane, x, y, blockX, blockY));
                activeBlocks.add(blockHash);

                setState(x, y, ADDED);
            }
        }
    }

    /**
     *
     * @param regions
     * @param position
     * @param distance
     */
    private void update(RegionSet regions, Position position, int distance) {
        Iterator<Node> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();

            int blockX = node.getBlockX(), blockY = node.getBlockY(), plane = node.getPlane();
            int blockHash = plane << 24 | blockX << 12 | blockY;

            Position compare = new Position(blockX << 3, blockY << 3, plane);

            if (!position.within(compare, distance)) {
                activeBlocks.remove(blockHash);
                iterator.remove();
                continue;
            }

            int localX = node.getX(), localY = node.getY();

            switch (getState(localX, localY)) {
                case ADDED:
                    setState(localX, localY, REBUILD);
                    break;

                default:
                    if(regions.isBlockUpdated(plane, blockX, blockY)) {
                        setState(localX, localY, UPDATED);
                    } else {
                        setState(localX, localY, IDLE);
                    }
                    break;
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    private void setState(int x, int y, int state) {
        state &= (1 << BITS_PER_STATE) - 1;

        int off = (x * LENGTH) + y;
        int start = off * BITS_PER_STATE, end = start + BITS_PER_STATE;

        // Clear the bits
        states.clear(start, end);

        // Set each of the bits
        for(int i = 0; i < BITS_PER_STATE; i++) {
            states.set(start + i, (state & 1 << i) != 0);
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int getState(int x, int y) {
        int off = (x * LENGTH) + y;
        int start = off * BITS_PER_STATE, end = start + BITS_PER_STATE;
        byte[] bytes = states.get(start, end).toByteArray();
        if(bytes.length < 1) {
            return 0;
        }
        return bytes[0] & (1 << BITS_PER_STATE) - 1;
    }
}