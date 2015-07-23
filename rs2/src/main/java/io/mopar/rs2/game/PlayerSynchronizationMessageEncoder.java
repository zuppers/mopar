package io.mopar.rs2.game;

import io.mopar.game.model.Appearance;
import io.mopar.game.model.Position;
import io.mopar.game.model.Step;
import io.mopar.game.msg.ChatMessage;
import io.mopar.game.msg.PlayerSynchronizationMessage;
import io.mopar.game.sync.*;
import io.mopar.game.sync.block.AppearanceUpdateBlock;
import io.mopar.game.sync.block.ChatUpdateBlock;
import io.mopar.game.sync.player.*;
import io.mopar.rs2.msg.MessageEncoder;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerSynchronizationMessageEncoder implements MessageEncoder<PlayerSynchronizationMessage> {

    interface PlayerDescriptorEncoder<T extends PlayerDescriptor> {
        void encode(T descriptor, PacketBuilder builder);
    }

    interface UpdateBlockEncoder<T extends UpdateBlock> {
        void encode(T descriptor, PacketBuilder builder);
    }

    /**
     * The descriptor encoders.
     */
    private Map<Class<? extends PlayerDescriptor>, PlayerDescriptorEncoder> descriptorEncoders = new HashMap<>();

    /**
     * The block encoders.
     */
    private Map<Class<? extends UpdateBlock>, UpdateBlockEncoder> blockEncoders = new HashMap<>();

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

        register(ChatUpdateBlock.class, this::encodeChatUpdateBlock);
        register(AppearanceUpdateBlock.class, this::encodeAppearanceUpdateBlock);
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
        PlayerDescriptor selfDescriptor = message.getSelfDescriptor();
        encodeDescriptor(selfDescriptor, builder);

        // Write out the descriptors for every player of interest, inview and new
        builder.writeBits(8, message.getLocalPlayerCount());
        for(PlayerDescriptor descriptor : message.getDescriptors()) {
            encodeDescriptor(descriptor, builder);
        }
        builder.writeBits(11, 2047);

        builder.switchToByteAccess();

        if(selfDescriptor.hasUpdateBlocks()) {
            encodeBlocks(selfDescriptor, builder);
        }

        for(PlayerDescriptor descriptor : message.getDescriptors()) {
            if(descriptor.hasUpdateBlocks() && descriptor.getClass() != RemovedPlayerDescriptor.class) {
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
    private <T extends PlayerDescriptor> void register(Class<T> descriptorClass, PlayerDescriptorEncoder<T> encoder) {
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
    private void encodeDescriptor(PlayerDescriptor descriptor, PacketBuilder builder) {
        PlayerDescriptorEncoder encoder = descriptorEncoders.get(descriptor.getClass());
        encoder.encode(descriptor, builder);
    }

    /**
     * Encodes the blocks for a descriptor.
     *
     * @param descriptor
     * @param builder
     */
    private void encodeBlocks(PlayerDescriptor descriptor, PacketBuilder builder) {
        int flags = 0;

        if(descriptor.hasUpdateBlock(ChatUpdateBlock.class)) {
            flags |= 0x80;
        }

        if(descriptor.hasUpdateBlock(AppearanceUpdateBlock.class)) {
            flags |= 0x4;
        }

        if(flags > 0xff) {
            flags |= 0x10;
            builder.writeLEShort(flags);
        } else {
            builder.writeByte(flags);
        }

        encodeBlock(descriptor.getUpdateBlock(ChatUpdateBlock.class), builder);
        encodeBlock(descriptor.getUpdateBlock(AppearanceUpdateBlock.class), builder);
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

        Queue<Step> steps = descriptor.getSteps();
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

    /**
     *
     * @param block
     * @param builder
     */
    private void encodeChatUpdateBlock(ChatUpdateBlock block, PacketBuilder builder) {
        ChatMessage message = block.getMessage();

        builder.writeLEShort((message.getColor() << 8) | message.getEffect());
        builder.writeByte(2);                                                           // Rights

        byte[] bytes = message.getBytes();
        builder.writeByte(bytes.length);
        builder.writeBytesReverse(bytes);
    }

    /**
     *
     * @param block
     * @param builder
     */
    private void encodeAppearanceUpdateBlock(AppearanceUpdateBlock block, PacketBuilder builder) {
        int start = builder.writerIndex();
        builder.writeByte(0);

        int flags = block.isMale() ? 1 : 0;
        builder.writeByte(flags);                       // Flags
        builder.writeByte(-1);
        builder.writeByte(-1);

        // Helmet
        if(block.hasHelmet()) {
            builder.writeShort(0x8000 | block.getHelmetId());
        } else {
            builder.writeByte(0);
        }

        // Cape
        if(block.hasCape()) {
            builder.writeShort(0x8000 | block.getCapeId());
        } else {
            builder.writeByte(0);
        }

        // Neck
        if(block.hasNecklace()) {
            builder.writeShort(0x8000 | block.getNecklaceId());
        } else {
            builder.writeByte(0);
        }

        // Weapon
        if(block.hasWeapon()) {
            builder.writeShort(0x8000 | block.getWeaponId());
        } else {
            builder.writeByte(0);
        }

        // Body
        if(block.hasChest()) {
            builder.writeShort(0x8000 | block.getChestId());
        } else if(block.hasBodyStyle()) {
            builder.writeShort(0x100 | block.getBodyStyle());
        } else {
            builder.writeByte(0);
        }

        // Shield
        if(block.hasShield()) {
            builder.writeShort(0x8000 | block.getShieldId());
        } else {
            builder.writeByte(0);
        }

        // Arms
        if(block.hasArmsStyle()) {
            builder.writeShort(0x100 | block.getArmsStyle());
        } else {
            builder.writeByte(0);
        }

        // Legs
        if(block.hasPants()) {
            builder.writeShort(0x8000 | block.getPantsId());
        } else if(block.hasLegsStyle()) {
            builder.writeShort(0x100 | block.getLegsStyle());
        } else {
            builder.writeByte(0);
        }

        // Head
        if(block.hasHeadStyle()) {
            builder.writeShort(0x100 | block.getHeadStyle());
        } else {
            builder.writeByte(0);
        }

        // Hands
        if(block.hasGloves()) {
            builder.writeShort(0x8000 | block.getGlovesId());
        } else if(block.hasHandsStyle()) {
            builder.writeShort(0x100 | block.getHandsStyle());
        } else {
            builder.writeByte(0);
        }

        // Feet
        if(block.hasShoes()) {
            builder.writeShort(0x8000 | block.getShoesId());
        } else if(block.hasHandsStyle()) {
            builder.writeShort(0x100 | block.getFeetStyle());
        } else {
            builder.writeByte(0);
        }

        // Beard
        if(block.hasBeardStyle()) {
            builder.writeShort(0x100 | block.getBeardStyle());
        } else {
            builder.writeByte(0);
        }

        for (int i = 0; i < 5; i++) {
            builder.writeByte(0);
        }

        builder.writeShort(block.getStance());
        builder.writeLong(block.getName());
        builder.writeByte(block.getCombatLevel());

        // Need to research what these exactly do
        builder.writeByte(0);
        builder.writeByte(0);
        builder.writeByte(0);

        // Write the length of the block
        int len = builder.writerIndex() - start - 1;
        builder.putByteA(start, len);
    }
}
