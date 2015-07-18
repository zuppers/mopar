package io.mopar.rs2.net.packet;

import io.netty.buffer.ByteBuf;

/**
 * @author Hadyn Fitzgerald
 */
public class Packet {

    /**
     * The meta data.
     */
    private PacketMetaData meta;

    /**
     * The buffer.
     */
    private ByteBuf buffer;

    /**
     * Constructs a new {@link Packet};
     *
     * @param meta The packet meta data.
     * @param buffer The packet buffer.
     */
    public Packet(PacketMetaData meta, ByteBuf buffer) {
        this.meta = meta;
        this.buffer = buffer;
    }

    /**
     * Gets the meta data.
     *
     * @return The meta data.
     */
    public PacketMetaData getMeta() {
        return meta;
    }

    /**
     * Helper method; gets the packet id.
     *
     * @return The id.
     */
    public int getId() { return meta.getId(); }

    /**
     * Helper method; gets the packet length.
     *
     * @return The length.
     */
    public int getLength() { return meta.getLength(); }

    /**
     * Gets the buffer.
     *
     * @return The buffer.
     */
    public ByteBuf getBuffer() {
        return buffer;
    }
}
