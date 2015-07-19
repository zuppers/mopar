package io.mopar.rs2.game;

import io.mopar.game.model.Position;
import io.mopar.game.model.Step;
import io.mopar.game.msg.PlayerSynchronizationMessage;
import io.mopar.game.sync.*;
import io.mopar.game.sync.player.*;
import io.mopar.rs2.msg.MessageEncoder;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerSynchronizationMessageEncoder implements MessageEncoder<PlayerSynchronizationMessage> {

    interface PlayerDescriptorEncoder<T extends PlayerDescriptor> {
        void encode(T descriptor, PacketBuilder builder);
    }

    /**
     * The descriptor encoders.
     */
    private Map<Class<? extends PlayerDescriptor>, PlayerDescriptorEncoder> encoders = new HashMap<>();

    /**
     * Constructs a new {@link PlayerSynchronizationMessageEncoder};
     */
    public PlayerSynchronizationMessageEncoder() {
        register(IdlePlayerDescriptor.class, this::encodeIdleDescriptor);
        register(WalkingPlayerDescriptor.class, this::encodeWalkingDescriptor);
        register(RunningPlayerDescriptor.class, this::encodeRunningDescriptor);
        register(TeleportingPlayerDescriptor.class, this::encodeTeleportDescriptor);
        register(AddPlayerDescriptor.class, this::encodeAddDescriptor);
        register(RemovedPlayerDescriptor.class, this::encodeRemoveDescriptor);
    }

    /**
     * Encodes a player synchronization message to a packet.
     *
     * @param allocator The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param message The message to encode.
     * @return The encoded packet.
     */
    @Override
    public Packet encode(ByteBufAllocator allocator, PacketMetaList outgoingPackets,
                                              PlayerSynchronizationMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("player_update"), allocator);

        builder.switchToBitAccess();

        // Write the self descriptor
        encodeDescriptor(message.getSelfDescriptor(), builder);

        // Write out the descriptors for every player of interest, inview and new
        builder.writeBits(8, message.getLocalPlayerCount());
        for(PlayerDescriptor descriptor : message.getDescriptors()) {
            encodeDescriptor(descriptor, builder);
        }
        builder.writeBits(11, 2047);                                                        // END OF DATA

        builder.switchToByteAccess();

        return builder.build();
    }

    /**
     * Registers a descriptor encoder.
     *
     * @param descriptorClass The descriptor class.
     * @param encoder The encoder.
     * @param <T> The generic descriptor type.
     */
    private <T extends PlayerDescriptor> void register(Class<T> descriptorClass, PlayerDescriptorEncoder<T> encoder) {
        encoders.put(descriptorClass, encoder);
    }

    /**
     * Encodes a descriptor.
     *
     * @param descriptor The descriptor to encode.
     * @param builder The packet builder to encode the descriptor to.
     */
    private void encodeDescriptor(PlayerDescriptor descriptor, PacketBuilder builder) {
        PlayerDescriptorEncoder encoder = encoders.get(descriptor.getClass());
        encoder.encode(descriptor, builder);
    }

    /**
     * Encodes an idle player descriptor. For an idle player if there are no update blocks then we do not write out
     * the descriptor type however if there is we must. For other descriptors note that we have to write out if there
     * were update blocks.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeIdleDescriptor(IdlePlayerDescriptor descriptor, PacketBuilder builder) {
        boolean bool = descriptor.hasUpdateBlocks();
        builder.writeBits(1, bool ? 1 : 0);
        if(bool) {
            builder.writeBits(2, 0);
        }
    }

    /**
     * Encodes a walking player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeWalkingDescriptor(WalkingPlayerDescriptor descriptor, PacketBuilder builder) {
        builder.writeBits(1, 1);
        builder.writeBits(2, 1);
        builder.writeBits(3, descriptor.getStep().toInteger());
        builder.writeBit(descriptor.hasUpdateBlocks());
    }

    /**
     * Encodes a running player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeRunningDescriptor(RunningPlayerDescriptor descriptor,
                                         PacketBuilder builder) {
        builder.writeBits(1, 1);
        builder.writeBits(2, 2);

        List<Step> steps = descriptor.getSteps();
        builder.writeBit(steps.size() > 1);

        for(Step step : steps) {
            builder.writeBits(3, step.toInteger());
        }

        builder.writeBit(descriptor.hasUpdateBlocks());
    }

    /**
     * Encodes a teleporting player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeTeleportDescriptor(TeleportingPlayerDescriptor descriptor,
                                          PacketBuilder builder) {
        builder.writeBits(1, 1);
        builder.writeBits(2, 3);

        Position position = descriptor.getPosition();

        builder.writeBits(7, position.getLocalY(descriptor.getRelativePosition()));
        builder.writeBits(1, descriptor.getClearWaypoints() ? 1 : 0);
        builder.writeBits(2, position.getPlane());
        builder.writeBit(descriptor.hasUpdateBlocks());
        builder.writeBits(7, position.getLocalX(descriptor.getRelativePosition()));
    }

    /**
     * Encodes a remove player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeRemoveDescriptor(PlayerDescriptor descriptor, PacketBuilder builder) {
        builder.writeBits(1, 1);
        builder.writeBits(2, 3);
    }

    /**
     * Encodes an add player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeAddDescriptor(AddPlayerDescriptor descriptor, PacketBuilder builder) {
        int x = descriptor.getPosition().getX() - descriptor.getRelative().getX();
        int y = descriptor.getPosition().getY() - descriptor.getRelative().getY();

        builder.writeBits(11, descriptor.getPlayerId());
        builder.writeBits(1, descriptor.hasUpdateBlocks() ? 1 : 0);
        builder.writeBits(5, x);
        builder.writeBits(3, descriptor.getLastStep().toInteger());
        builder.writeBits(1, 0);
        builder.writeBits(5, y);
    }
}
