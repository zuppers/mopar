package io.mopar.rs2.net.packet;

/**
 * Created by hadyn on 6/25/2015.
 */
public class PacketMetaData {
    public static final int VAR_BYTE_LENGTH = -1;
    public static final int VAR_SHORT_LENGTH = -2;

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * The length of the packet.
     */
    private int length;

    /**
     * Constructs a new {@link PacketMetaData};
     *
     * @param id The id of the packet or <code>-1</code> if the packet doesn't have an id.
     * @param name The name of the packet.
     * @param length The length of the packet.
     */
    public PacketMetaData(int id, String name, int length) {
        this.id = id;
        this.name = name;
        this.length = length;
    }

    /**
     * Gets the id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the length.
     *
     * @return The length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
}
