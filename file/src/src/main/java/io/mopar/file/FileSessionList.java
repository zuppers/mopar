package io.mopar.file;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Hadyn Fitzgerald
 */
public class FileSessionList implements Iterable<FileSession> {

    private class SessionListIterator implements Iterator<FileSession> {
        int cursor = -1;
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
        public FileSession next() {
            if(check != modifiedCount) {
                throw new ConcurrentModificationException();
            }
            for(++cursor; cursor < capacity; cursor++) {
                if(sessions[cursor] != null) {
                    break;
                }
            }
            count++;
            return sessions[cursor];
        }
    }

    /**
     * The entities.
     */
    private FileSession[] sessions;

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
     * Constructs a new {@link FileSessionList};
     *
     * @param capacity The capacity.
     */
    public FileSessionList(int capacity) {
        sessions = new FileSession[capacity];
        this.capacity = capacity;
    }

    /**
     * Adds an session.
     *
     * @param session The session to add.
     * @return If the session was successfully added.
     */
    public boolean add(FileSession session) {
        if(size >= capacity) {
            return false;
        }
        int id = getNextId();
        sessions[id] = session;
        session.setId(id);
        modifiedCount++;
        size++;
        return true;
    }

    /**
     * Removes an session.
     *
     * @param id The id of the session.
     */
    public void remove(int id) {
        if(id < 0 || id >= capacity) {
            throw new ArrayIndexOutOfBoundsException(id);
        }
        FileSession session = sessions[id];
        sessions[id] = null;
        session.setId(-1);
        modifiedCount++;
        size--;

    }

    /**
     * Gets if a session exists.
     *
     * @param id The id of the session.
     * @return If the session exists in the list.
     */
    public boolean exists(int id) {
        return sessions[id] != null;
    }

    /**
     * Gets an session.
     *
     * @param id The id of the session.
     * @return The session.
     */
    public FileSession get(int id) {
        if(id < 0 || id >= capacity) {
            throw new ArrayIndexOutOfBoundsException(id);
        }
        return sessions[id];
    }

    /**
     * Gets if a session is contained within this list.
     *
     * @param id The session id.
     * @return If the session at the specified id is not <code>null</code>.
     */
    public boolean contains(int id) {
        return sessions[id] != null;
    }


    /**
     * Gets a stream of the sessions.
     *
     * @return The stream.
     */
    public Stream<FileSession> stream() {
        return Stream.of(sessions).filter(session -> session != null);
    }

    /**
     * Gets the iterator for this list.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<FileSession> iterator() {
        return new SessionListIterator();
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
     * Gets the list capacity.
     *
     * @return The capacity.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Clears the list.
     */
    public void clear() {
        for(int id = 0; id < capacity; id++) {
            if(size < 1) {
                break;
            }
            if(sessions[id] != null) {
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
        for(int i = 0; i < sessions.length; i++) {
            if(sessions[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
