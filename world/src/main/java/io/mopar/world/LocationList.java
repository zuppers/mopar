package io.mopar.world;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * @author Hadyn Fitzgerald
 */
public class LocationList implements Iterable<Location> {

    private class LocationListIterator implements Iterator<Location> {
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
        public Location next() {
            if(check != modifiedCount) {
                throw new ConcurrentModificationException();
            }
            for(++pointer; pointer < capacity; pointer++) {
                if(locations[pointer] != null) {
                    break;
                }
            }
            count++;
            return locations[pointer];
        }
    }

    /**
     * The locations.
     */
    private Location[] locations;

    /**
     * The capacity.
     */
    private int capacity;

    /**
     * The modification count.
     */
    private int modifiedCount;

    /**
     * The current size.
     */
    private int size;

    /**
     * Constructs a new {@link LocationList};
     *
     * @param capacity The capacity.
     */
    public LocationList(int capacity) {
        locations = new Location[capacity];
        this.capacity = capacity;
    }

    /**
     * Adds a location.
     *
     * @param location The location to add.
     * @return If the location was added successfully or <code>false</code> if the list has reached its capacity.
     */
    public boolean add(Location location) {
        if(size >= capacity) {
            return false;
        }
        int id = getNextId();
        locations[id] = location;
        location.setId(id);
        modifiedCount++;
        size++;
        return true;
    }

    /**
     * Gets a location by its id.
     *
     * @param id The id.
     * @return The location for the given id.
     */
    public Location get(int id) {
        if(id < 0 || id >= capacity) {
            throw new ArrayIndexOutOfBoundsException(id);
        }
        return locations[id];
    }

    @Override
    public Iterator<Location> iterator() {
        return new LocationListIterator();
    }

    /**
     * Gets the amount of entries in the list.
     *
     * @return The current size.
     */
    public int size() {
        return size;
    }

    /**
     * Gets the next free id.
     *
     * @return The next id.
     */
    private int getNextId() {
        for(int i = 0; i < capacity; i++) {
            if(locations[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
