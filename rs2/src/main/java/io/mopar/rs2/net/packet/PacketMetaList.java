package io.mopar.rs2.net.packet;

import java.util.*;

/**
 * @author Hadyn Fitzgerald
 */
public class PacketMetaList {

    /**
     * The meta information mapped by its name.
     */
    private Map<Integer, PacketMetaData> meta = new HashMap<>();

    /**
     * Constructs a new {@link PacketMetaList};
     */
    public PacketMetaList() {}

    /**
     * Adds a packet to the meta data list.
     *
     * @param data The packet meta data.
     */
    public void add(PacketMetaData data) {
        meta.put(data.getId(), data);
    }

    /**
     * Gets if the meta data list contains a packet by an id.
     *
     * @param id The id of the packet.
     * @return If the list contains a packet with the provided id.
     */
    public boolean contains(int id) {
        return meta.containsKey(id);
    }

    /**
     * Gets a packet from its id.
     *
     * @param id The id of the packet.
     * @return The packet.
     */
    public PacketMetaData get(int id) {
        if(!contains(id)) {
            throw new NullPointerException("No such packet for the given id");
        }
        return meta.values().stream().filter(data -> data.getId() == id).findFirst().get();
    }

    /**
     * Helper method; gets the name of a packet from its id.
     *
     * @param id The id of the packet.
     * @return The name of the packet.
     * @throws NullPointerException if no packet exists for the provided id.
     */
    public String getName(int id) {
        if(!contains(id)) {
            throw new NullPointerException("No such packet for the given id");
        }
        return get(id).getName();
    }

    /**
     * Helper method; gets the length of a packet from its id.
     *
     * @param id The id of the packet.
     * @return The length of the packet.
     * @throws NullPointerException if no packet exists for the provided id.
     */
    public int getLength(int id) {
        if(!contains(id)) {
            throw new NullPointerException("No such packet for the given id.");
        }
        return get(id).getLength();
    }

    /**
     * Gets a packet from its name.
     *
     * @param name The name of the packet.
     * @return The packet for the given name.
     */
    public PacketMetaData get(String name) {
        return meta.values().stream().filter(data -> name.equals(data.getName())).findFirst().get();
    }

    /**
     * Helper method; gets the id of a packet from its name.
     *
     * @param name The name of the packet.
     * @return The id of the packet or <code>-1</code> if no packet exists by the provided name.
     */
    public int getId(String name) {
        PacketMetaData data = get(name);
        if(data == null) {
            return -1;
        }
        return data.getId();
    }

}
