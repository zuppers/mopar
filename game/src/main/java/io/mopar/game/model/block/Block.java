package io.mopar.game.model.block;

import io.mopar.game.model.*;

import java.util.*;

/**
 * @author Hadyn Fitzgerald
 */
public class Block {

    /**
     * The tiles.
     */
    private BlockTile[][] tiles  = new BlockTile[8][8];

    /**
     * The modified locales.
     */
    private Set<Integer> updatedObjectTiles = new HashSet<>();

    /**
     *
     */
    private Region parent;

    /**
     *
     */
    private int plane;


    /**
     *
     */
    private int localX;

    /**
     *
     */
    private int localY;

    /**
     * The events.
     */
    private Queue<BlockEvent> events = new LinkedList<>();

    /**
     *
     */
    private boolean updated;

    /**
     *
     * @param parent
     * @param plane
     * @param localX
     * @param localY
     */
    public Block(Region parent, int plane, int localX, int localY) {
        this.parent = parent;
        this.plane = plane;
        this.localX = localX;
        this.localY = localY;
    }

    /**
     *
     * @return
     */
    public int getRelativeX() {
        return (localX + (parent.getX() << 3)) << 3;
    }

    /**
     *
     * @return
     */
    public int getRelativeY() {
        return (localY + (parent.getY() << 3)) << 3;
    }

    /**
     *
     * @param x
     * @param y
     * @param type
     * @param configId
     * @param orientation
     */
    public void updateGameObject(int x, int y, int type, int configId, int orientation) {
        int group = GameObjectGroup.getGroupForType(type);
        BlockTile tile = getTile(x, y);
        int hash = group << 12 | x << 6 | y;

        GameObject obj = tile.getGameObject(group);

        if(obj == null) {
            obj = new GameObject(configId, type, orientation);
            obj.setPosition(new Position(getRelativeX() + x, getRelativeY() + y, plane));
            tile.setLocale(group, obj);
            updatedObjectTiles.add(hash);
            System.out.println("Created an obj");
        } else {
            parent.unmarkObject(plane,
                    obj.getPosition().getLocalRegionX(),
                    obj.getPosition().getLocalRegionY(),
                    obj.getType(),
                    obj.getConfigId(),
                    obj.getOrientation());

            obj.setType(type);
            obj.setConfig(configId);
            obj.setOrientation(orientation);

            if(obj.isNormal()) {
                updatedObjectTiles.remove(hash);
            } else {
                updatedObjectTiles.add(hash);
            }

            if(!obj.isNatural() && obj.isNormal()) {
                tile.removeGameObject(group);
            }
        }

        if(configId != -1) {
            parent.markObject(plane,
                    obj.getPosition().getRegionX(),
                    obj.getPosition().getRegionY(),
                    type,
                    configId,
                    orientation);

            addEvent(new GameObjectUpdatedEvent(obj));
        } else {
            addEvent(new GameObjectRemovedEvent(obj));
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param group
     */
    public void removeGameObject(int x, int y, int group) {
        GameObject gameObject = getGameObject(x, y, group);
        if(gameObject != null) {
            updateGameObject(x, y, gameObject.getType(), GameObject.REMOVED, gameObject.getOrientation());
        }
    }

    /**
     *
     * @param event
     */
    public void addEvent(BlockEvent event) {
        events.add(event);
        parent.addUpdated(this);
        updated = true;
    }

    /**
     *
     * @return
     */
    public Queue<BlockEvent> getEvents() {
        return events;
    }

    /**
     *
     * @param x
     * @param y
     * @param group
     * @return
     */
    public GameObject getGameObject(int x, int y, int group) {
        if(tiles[x][y] == null) {
            return null;
        }
        BlockTile tile = getTile(x, y);
        return tile.getGameObject(group);
    }

    /**
     * Gets the tile at the specified coordinates. If the tile does not exist,
     * one is created.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the tile.
     */
    public BlockTile getTile(int x, int y) {
        BlockTile tile = tiles[x][y];
        if(tile == null) {
            tile = tiles[x][y] = new BlockTile(x, y);
        }
        return tile;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getUpdatedObjectTiles() {
        return updatedObjectTiles;
    }

    /**
     *
     * @return
     */
    public boolean isUpdated() { return updated; }

    /**
     * Resets the block.
     */
    public void reset() {
        events.clear();
        updated = false;
    }
}
