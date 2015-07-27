package io.mopar.rs2.net.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

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
     * Parses a JSON element.
     *
     * @param element the element to parse.
     */
    public void parse(JsonElement element) {
        if(!element.isJsonArray()) {
            throw new IllegalArgumentException("Expecting element to be JSON array");
        }

        JsonArray array = element.getAsJsonArray();

        for(JsonElement ele : array) {
            if(!ele.isJsonObject()) {
                throw new RuntimeException("Expecting array element to be JSON object");
            }

            // Extract the packet meta data from the JSON object
            JsonObject obj = ele.getAsJsonObject();
            int id = obj.get("id").getAsInt();
            String name = obj.get("name").getAsString();
            int length = obj.get("length").getAsInt();

            // Append the meta data
            add(new PacketMetaData(id, name, length));
        }
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