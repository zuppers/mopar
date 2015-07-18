package io.mopar.world;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldList implements Iterable<World> {

    private class WorldListIterator implements Iterator<World> {
        int pointer = -1;
        int check = modifiedCount;
        int count;

        @Override
        public boolean hasNext() {
            if(check != modifiedCount) {
                throw new ConcurrentModificationException();
            }
            return count < size;
        }

        @Override
        public World next() {
            if(check != modifiedCount) {
                throw new ConcurrentModificationException();
            }
            for(++pointer; pointer < capacity; pointer++) {
                if(worlds[pointer] != null) {
                    break;
                }
            }
            count++;
            return worlds[pointer];
        }
    }

    /**
     * The worlds.
     */
    private World[] worlds;

    /**
     * The modification count.
     */
    private int modifiedCount = 0;

    /**
     * The capacity.
     */
    private int capacity;

    /**
     * The size of the list.
     */
    private int size;

    /**
     * The minimum id.
     */
    private int minimumId;

    /**
     * The maximum id.
     */
    private int maximumId;

    /**
     * Constructs a new {@link WorldList};
     *
     * @param capacity The list capacity.
     */
    public WorldList(int capacity) {
        worlds = new World[capacity];
        this.capacity = capacity;
        minimumId = capacity;
        maximumId = -1;
    }

    /**
     * Adds a world to the list.
     *
     * @param world The world.
     * @return If the world was successfully added.
     */
    public boolean add(World world) {
        if(size >= capacity) {
            return false;
        }
        set(getNextId(), world);
        return true;
    }

    /**
     *
     * @param id
     * @param world
     * @return
     */
    public World set(int id, World world) {
        if(id < 0 || id >= capacity) {
            throw new ArrayIndexOutOfBoundsException(id);
        }

        World removed = null;
        if(worlds[id] != null) {
            removed = worlds[id];
            worlds[id] = null;
            removed.setId(-1);
        }

        worlds[id] = world;
        world.setId(id);
        modifiedCount++;
        size++;

        updateBounds();

        return removed;
    }

    /**
     * Removes a world.
     *
     * @param id The world id.
     */
    public World remove(int id) {
        World world = worlds[id];
        worlds[id] = null;
        world.setId(-1);
        modifiedCount++;
        size--;

        updateBounds();

        return world;
    }

    /**
     * Gets the list iterator.
     *
     * @return The list iterator.
     */
    @Override
    public Iterator<World> iterator() {
        return new WorldListIterator();
    }

    /**
     * Gets the size of the list.
     *
     * @return The size.
     */
    public int size() {
        return size;
    }

    /**
     * Gets the current minimum id.
     *
     * @return The minimum id.
     */
    public int getMinimumId() {
        return minimumId;
    }

    /**
     * Gets the current maximum id.
     *
     * @return The maximum id.
     */
    public int getMaximumId() {
        return maximumId;
    }

    /**
     * Updates the minimum and maximum id.
     */
    private void updateBounds() {
        if(size > 0) {
            for(minimumId = 0; minimumId < capacity; minimumId++) {
                if(worlds[minimumId] != null) {
                    break;
                }
            }

            for(maximumId = capacity - 1; maximumId >= 0; maximumId--) {
                if(worlds[maximumId] != null) {
                    break;
                }
            }
        } else {
            minimumId = capacity;
            maximumId = -1;
        }
    }

    /**
     * Gets the next free id.
     *
     * @return The next id.
     */
    private int getNextId() {
        for(int i = 0; i < capacity; i++) {
            if(worlds[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
