package io.mopar.game.model;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Hadyn Fitzgerald
 */
public class EntityList<T extends Entity> implements Iterable<T> {

    private class EntityListIterator implements Iterator<T> {
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
        public T next() {
            if(check != modifiedCount) {
                throw new ConcurrentModificationException();
            }
            for(++pointer; pointer < capacity; pointer++) {
                if(entities[pointer] != null) {
                    break;
                }
            }
            count++;
            return (T) entities[pointer];
        }
    }

    /**
     * The entities.
     */
    private Entity[] entities;

    /**
     * The capacity.
     */
    private int capacity;

    /**
     * The amount of elements in this list.
     */
    private int size;

    /**
     * The modified count.
     */
    private int modifiedCount;

    /**
     * Constructs a new {@link EntityList};
     *
     * @param capacity The capacity.
     */
    public EntityList(int capacity) {
        entities = new Entity[capacity];
        this.capacity = capacity;
    }

    /**
     * Adds an entity.
     *
     * @param entity The entity to add.
     * @return If the entity was successfully added.
     */
    public boolean add(T entity) {
        if(size >= capacity) {
            return false;
        }
        int id = getNextId();
        entities[id] = entity;
        entity.setId(id);
        modifiedCount++;
        size++;
        return true;
    }

    /**
     * Removes an entity.
     *
     * @param id The id of the entity.
     */
    public T remove(int id) {
        if(id < 0 || id >= capacity) {
            throw new ArrayIndexOutOfBoundsException(id);
        }
        Entity entity = entities[id];
        entities[id] = null;
        entity.setId(-1);
        modifiedCount++;
        size--;
        return (T) entity;
    }

    /**
     * Gets an entity.
     *
     * @param id The id of the entity.
     * @return The entity.
     */
    public T get(int id) {
        if(id < 0 || id >= capacity) {
            throw new ArrayIndexOutOfBoundsException(id);
        }
        return (T) entities[id];
    }


    /**
     * Gets a stream of the entities.
     *
     * @return The stream.
     */
    public Stream<T> stream() {
        return Stream.of(entities).filter(entity -> entity != null).map(entity -> (T) entity);
    }

    /**
     * Gets the iterator for this list.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new EntityListIterator();
    }

    /**
     * Gets if the list is empty.
     *
     * @return If the list is empty.
     */
    public boolean empty() {
        return size < 1;
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
     * Clears the list.
     */
    public void clear() {
        for(int id = 0; id < capacity; id++) {
            if(size < 1) {
                break;
            }
            if(entities[id] != null) {
                remove(id);
            }
        }
    }

    /**
     * Gets the next free id.
     *
     * @return The id or <code>-1</code> if there is no space left in the list.
     */
    private int getNextId() {
        for(int i = 1; i < entities.length; i++) {
            if(entities[i] == null) {
                return i;
            }
        }
        return -1;
    }
}