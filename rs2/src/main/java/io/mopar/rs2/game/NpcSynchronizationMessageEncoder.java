package io.mopar.rs2.game;

import io.mopar.game.model.Step;
import io.mopar.game.msg.NpcSynchronizationMessage;
import io.mopar.game.sync.NpcDescriptor;
import io.mopar.game.sync.UpdateBlock;
import io.mopar.game.sync.npc.*;
import io.mopar.rs2.msg.MessageEncoder;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by hadyn on 7/28/2015.
 */
public class NpcSynchronizationMessageEncoder implements MessageEncoder<NpcSynchronizationMessage> {

    interface NpcDescriptorEncoder<T extends NpcDescriptor> {
        void encode(T descriptor, PacketBuilder builder);
    }

    interface UpdateBlockEncoder<T extends UpdateBlock> {
        void encode(T descriptor, PacketBuilder builder);
    }

    /**
     * The descriptor encoders.
     */
    private Map<Class<? extends NpcDescriptor>, NpcDescriptorEncoder> descriptorEncoders = new HashMap<>();

    /**
     * The block encoders.
     */
    private Map<Class<? extends UpdateBlock>, UpdateBlockEncoder> blockEncoders = new HashMap<>();

    /**
     * Constructs a new {@link PlayerSynchronizationMessageEncoder};
     */
    public NpcSynchronizationMessageEncoder() {
        register(IdleNpcDescriptor.class, this::encodeIdleDescriptor);
        register(WalkingNpcDescriptor.class, this::encodeWalkingDescriptor);
        register(RunningNpcDescriptor.class, this::encodeRunningDescriptor);
        register(AddNpcDescriptor.class, this::encodeAddDescriptor);
        register(RemovedNpcDescriptor.class, this::encodeRemoveDescriptor);
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
                         NpcSynchronizationMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("npc_update"), allocator);

        builder.switchToBitAccess();

        // Write out the descriptors for every player of interest, inview and new
        builder.writeBits(8, message.getLocalNpcCount());
        for(NpcDescriptor descriptor : message.getDescriptors()) {
            encodeDescriptor(descriptor, builder);
        }
        builder.writeBits(15, 32767);

        builder.switchToByteAccess();

        for(NpcDescriptor descriptor : message.getDescriptors()) {
            if(descriptor.hasUpdateBlocks() && descriptor.getClass() != RemovedNpcDescriptor.class) {
                encodeBlocks(descriptor, builder);
            }
        }

        return builder.build();
    }

    /**
     * Registers a descriptor encoder.
     *
     * @param descriptorClass The descriptor class.
     * @param encoder The encoder.
     * @param <T> The generic descriptor type.
     */
    private <T extends NpcDescriptor> void register(Class<T> descriptorClass, NpcDescriptorEncoder<T> encoder) {
        descriptorEncoders.put(descriptorClass, encoder);
    }

    /**
     *
     * @param blockClass
     * @param encoder
     * @param <T>
     */
    private <T extends UpdateBlock> void register(Class<T> blockClass, UpdateBlockEncoder<T> encoder) {
        blockEncoders.put(blockClass, encoder);
    }

    /**
     * Encodes a descriptor.
     *
     * @param descriptor The descriptor to encode.
     * @param builder The packet builder to encode the descriptor to.
     */
    private void encodeDescriptor(NpcDescriptor descriptor, PacketBuilder builder) {
        NpcDescriptorEncoder encoder = descriptorEncoders.get(descriptor.getClass());
        encoder.encode(descriptor, builder);
    }

    /**
     * Encodes the blocks for a descriptor.
     *
     * @param descriptor
     * @param builder
     */
    private void encodeBlocks(NpcDescriptor descriptor, PacketBuilder builder) {
        int flags = 0;

        if(flags > 0xff) {
            flags |= 0x10;
            builder.writeLEShort(flags);
        } else {
            builder.writeByte(flags);
        }

    }

    /**
     *
     * @param block
     * @param builder
     */
    private void encodeBlock(UpdateBlock block, PacketBuilder builder) {
        if(block != null) {
            UpdateBlockEncoder encoder = blockEncoders.get(block.getClass());
            encoder.encode(block, builder);
        }
    }

    /**
     * Encodes an idle player descriptor. For an idle player if there are no update blocks then we do not write out
     * the descriptor type however if there is we must. For other descriptors note that we have to write out if there
     * were update blocks.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeIdleDescriptor(IdleNpcDescriptor descriptor, PacketBuilder builder) {
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
    private void encodeWalkingDescriptor(WalkingNpcDescriptor descriptor, PacketBuilder builder) {
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
    private void encodeRunningDescriptor(RunningNpcDescriptor descriptor,
                                         PacketBuilder builder) {
        builder.writeBits(1, 1);
        builder.writeBits(2, 2);

        Queue<Step> steps = descriptor.getSteps();
        builder.writeBit(steps.size() > 1);

        for(Step step : steps) {
            builder.writeBits(3, step.toInteger());
        }

        builder.writeBit(descriptor.hasUpdateBlocks());
    }

    /**
     * Encodes a remove player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeRemoveDescriptor(NpcDescriptor descriptor, PacketBuilder builder) {
        builder.writeBits(1, 1);
        builder.writeBits(2, 3);
    }

    /**
     * Encodes an add player descriptor.
     *
     * @param descriptor The descriptor.
     * @param builder The packet builder.
     */
    private void encodeAddDescriptor(AddNpcDescriptor descriptor, PacketBuilder builder) {
        int x = descriptor.getPosition().getX() - descriptor.getRelative().getX();
        int y = descriptor.getPosition().getY() - descriptor.getRelative().getY();

        builder.writeBits(15, descriptor.getNpcId());
        builder.writeBits(1, 0);
        builder.writeBits(3, descriptor.getLastStep().toInteger());
        builder.writeBits(1, descriptor.hasUpdateBlocks() ? 1 : 0);
        builder.writeBits(5, y);
        builder.writeBits(14, descriptor.getType());
        builder.writeBits(5, x);
    }
}
