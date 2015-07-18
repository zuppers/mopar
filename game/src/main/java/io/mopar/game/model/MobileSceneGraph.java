package io.mopar.game.model;

import java.util.*;

/**
 * @author Hadyn Fitzgerald
 */
public class MobileSceneGraph<T extends Mobile> {

    public static class Node<T extends Mobile> {
        T entity;
        int id;

        Node(T entity) {
            this.entity = entity;
            this.id = entity.getId();
        }

        public T getEntity() {
            return entity;
        }

        public int getId() {
            return id;
        }

        public boolean isRemoved() {
            return entity.isRemoved();
        }
    }

    /**
     * IDLE         - No change.
     * ADDED        - An entity was moved to being visible.
     * REMOVED      - An entity was removed from being visible.
     * TELEPORTED   - An entity teleported within the visible space.
     */
    public static final int STATE_IDLE       = 0x0;
    public static final int STATE_ADDED      = 0x1;
    public static final int STATE_REMOVED    = 0x2;
    public static final int STATE_TELEPORTED = 0x3;

    /**
     * Flag for if an entity is visible or not.
     */
    public static final int VISIBLE_FLAG = 0x4;

    /**
     * The limit to the amount of nodes.
     */
    public static final int MAXIMUM_SIZE = 255;

    /**
     * Number of bits per a state.
     */
    private static final int BITS_PER_STATE = 3;

    /**
     * The nodes.
     */
    private Set<Node<T>> nodes = new HashSet<>();

    /**
     * The active ids.
     */
    private Set<Integer> activeIds = new HashSet<>();

    /**
     * The internal player states.
     */
    private BitSet states;

    /**
     * The amount of active nodes.
     */
    private int size;

    /**
     * Constructs a new {@link MobileSceneGraph};
     *
     * @param capacity The capacity.
     */
    public MobileSceneGraph(int capacity) {
        states = new BitSet(capacity * BITS_PER_STATE);
    }

    /**
     * Updates the graph.
     *
     * @param entities The nodes.
     * @param scanPosition The position to scan from.
     * @param currentPosition The current position.
     * @param scanDistance The distance to scan.
     * @param viewDistance The distance that is marked as viewable.
     */
    public void update(EntityList<T> entities, Position scanPosition, Position currentPosition, int scanDistance, int viewDistance) {
        populate(entities, scanPosition, scanDistance);
        update(scanPosition, currentPosition, scanDistance, viewDistance);
    }

    /**
     * Gets the nodes of all the players within the scene.
     *
     * @return The player nodes.
     */
    public Set<Node> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    /**
     * Gets the state of an entity.
     *
     * @param id The entity id.
     * @return The state.
     */
    public int getState(int id) {
        int start = id * BITS_PER_STATE, end = start + BITS_PER_STATE;
        byte[] bytes = states.get(start, end).toByteArray();
        if(bytes.length < 1) {
            return 0;
        }
        return bytes[0] & (1 << BITS_PER_STATE - 1) - 1;
    }

    /**
     * Gets if an entity is visible.
     *
     * @param id The id of the entity.
     * @return If the entity is visible.
     */
    public boolean isVisible(int id) {
        return states.get(id * BITS_PER_STATE + BITS_PER_STATE - 1);
    }

    /**
     * Populates the graph.
     *
     * @param entities The nodes.
     * @param position The position to scan from.
     * @param scanDistance The distance to scan.
     */
    private void populate(EntityList<T> entities, Position position, int scanDistance) {
        for(T entity : entities) {
            if(activeIds.contains(entity.getId()) || !position.within(entity.getPosition(), scanDistance)) {
                continue;
            }
            int id = entity.getId();
            setState(id, STATE_IDLE);
            nodes.add(new Node(entity));
            activeIds.add(id);
        }
    }

    /**
     * Updates the graph nodes.
     *
     * @param scanPosition    The scan position.
     * @param currentPosition The current position.
     * @param scanDistance    The distance to scan.
     * @param viewDistance    The distance that is marked as viewable.
     */
    private void update(Position scanPosition, Position currentPosition, int scanDistance, int viewDistance) {
        Iterator<Node<T>> iterator = nodes.iterator();
        while (iterator.hasNext()){
            Node<T> node = iterator.next();
            T entity = node.getEntity();
            int id = node.getId();

            if (node.isRemoved()) {
                // If the player was not viewable remove it from the set, since it is of no interest now
                if (!isVisible(node.getId())) {
                    activeIds.remove(node.getId());
                    iterator.remove();
                }
                setState(id, STATE_REMOVED);
                size--;
                continue;
            }

            Position compare = entity.getPosition();
            if (isVisible(id)) {

                // Check if the entity is teleporting within the viewport, if the entity exited the viewport,
                // otherwise just set the entity as being idle
                if (entity.isTeleporting() && currentPosition.within(compare, viewDistance)) {
                    setState(id, STATE_TELEPORTED | VISIBLE_FLAG);
                } else if (!currentPosition.within(compare, viewDistance)) {
                    setState(id, STATE_REMOVED);
                    size--;
                } else {
                    setState(id, STATE_IDLE | VISIBLE_FLAG);
                }
            } else {

                // Check if the player exits the scene, if they do just remove them
                if (!scanPosition.within(compare, scanDistance)) {
                    activeIds.remove(node.getId());
                    iterator.remove();
                    continue;
                }

                // If the player enters the viewport and there is enough room to set the player as viewable
                // then mark the player as added
                if (currentPosition.within(compare, viewDistance)) {
                    if (size >= MAXIMUM_SIZE) {
                        continue;
                    }

                    setState(node.getId(), STATE_ADDED | VISIBLE_FLAG);
                    size++;
                    continue;
                }

                setState(node.getId(), STATE_IDLE);
            }
        }
    }

    /**
     * Sets the state of an entity.
     *
     * @param id The entity id.
     * @param state The state.
     */
    private void setState(int id, int state) {
        state &= (1 << BITS_PER_STATE) - 1;

        int start = id * BITS_PER_STATE, end = start + BITS_PER_STATE;

        // Clear the bits
        states.clear(start, end);

        // Set each of the bits
        for(int i = 0; i < BITS_PER_STATE; i++) {
            states.set(start + i, (state & 1 << i) != 0);
        }
    }
}