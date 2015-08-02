package io.mopar.rs2.world;

import io.mopar.rs2.msg.MessageCodecContext;
import io.mopar.rs2.msg.MessageCodecInitializer;
import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.msg.handshake.WorldServiceHandshake;
import io.mopar.rs2.msg.world.SnapshotMessage;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.mopar.world.*;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldMessageCodecInitializer implements MessageCodecInitializer {

    /**
     * Constructs a new {@link WorldMessageCodecInitializer};
     */
    public WorldMessageCodecInitializer() {}

    @Override
    public void initialize(MessageCodec codec, PacketMetaList incomingPackets, PacketMetaList outgoingPackets) {
        registerMessageDecoders(codec, incomingPackets);
        registerMessageEncoders(codec, outgoingPackets);
    }

    /**
     * Register all of the message decoders to the provided codec.
     *
     * @param codec The message codec.
     * @param incomingPackets The incoming packets.
     */
    private void registerMessageDecoders(MessageCodec codec, PacketMetaList incomingPackets) {
        codec.registerMessageDecoder(incomingPackets.getId("worldlist_handshake"), this::decodeWorldHandshake);
    }

    /**
     * Decodes a world service handshake.
     *
     * @param packet The packet to decode.
     * @return The decoded message.
     */
    private WorldServiceHandshake decodeWorldHandshake(Packet packet) {
        return new WorldServiceHandshake(packet.getBuffer().readInt());
    }

    /**
     * Registers all of the message encoders to the provided codec.
     *
     * @param codec The message codec.
     * @param outgoingPackets The outgoing packets.
     */
    private void registerMessageEncoders(MessageCodec codec, PacketMetaList outgoingPackets) {
        codec.registerMessageEncoder(SnapshotMessage.class, this::encodeSnapshot);
    }

    /**
     * Encodes a snapshot message.
     *
     * @param alloc The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param message The message to encode.
     * @return The encoded packet.
     */
    private Packet encodeSnapshot(MessageCodecContext context, ByteBufAllocator alloc, PacketMetaList outgoingPackets, SnapshotMessage message) {
        PacketBuilder builder = PacketBuilder.create(0, "world_snapshot", PacketMetaData.VAR_SHORT_LENGTH, alloc);

        Snapshot snapshot = message.getSnapshot();

        boolean updated = message.isWorldsUpdated() || message.isPopulationUpdated();
        builder.writeBoolean(updated);

        if(updated) {
            builder.writeBoolean(message.isWorldsUpdated());

            WorldList worlds = snapshot.getWorlds();

            if(message.isWorldsUpdated()) {

                // Write the locations
                LocationList locations = snapshot.getLocations();
                builder.writeSmart(locations.size());
                for(int i = 0; i < locations.size(); i++) {
                    Location location = locations.get(i);
                    builder.writeSmart(location.getFlag());
                    builder.writeJstr2(location.getName());
                }

                // Write the worlds
                builder.writeSmart(worlds.getMinimumId());
                builder.writeSmart(worlds.getMaximumId());
                builder.writeSmart(worlds.size());

                for(World world : worlds) {
                    builder.writeSmart(world.getId() - worlds.getMinimumId());

                    Location location = world.getLocation();
                    builder.writeByte(location.getId());

                    builder.writeInt(world.getAttributes());
                    builder.writeJstr2(world.getActivity());
                    builder.writeJstr2(world.getAddress());
                }

                builder.writeInt(snapshot.getRevision());
            }

            for(World world : worlds) {
                builder.writeSmart(world.getId() - worlds.getMinimumId());
                builder.writeShort(world.getPopulation());
            }
        }

        return builder.build();
    }
}
