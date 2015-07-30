package io.mopar.rs2.game;

import io.mopar.game.model.Inventory;
import io.mopar.game.model.Item;
import io.mopar.game.model.Position;
import io.mopar.game.model.Scene;
import io.mopar.game.msg.*;
import io.mopar.rs2.msg.game.InterfaceItemOptionMessage;
import io.mopar.rs2.msg.MessageCodec;
import io.mopar.rs2.msg.MessageCodecInitializer;
import io.mopar.rs2.msg.game.*;
import io.mopar.rs2.net.packet.Packet;
import io.mopar.rs2.net.packet.PacketBuilder;
import io.mopar.rs2.net.packet.PacketMetaList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static io.mopar.rs2.util.ByteBufUtil.*;

/**
 * @author Hadyn Fitzgerald
 */
public class GameMessageCodecInitializer implements MessageCodecInitializer {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(GameMessageCodecInitializer.class);

    /**
     * The landscape key table.
     */
    private LandscapeKeyTable landscapeKeys;

    /**
     * Constructs a new {@link GameMessageCodecInitializer};
     *
     * @param landscapeKeys The landscape keys.
     */
    public GameMessageCodecInitializer(LandscapeKeyTable landscapeKeys) {
        this.landscapeKeys = landscapeKeys;
    }

    @Override
    public void initialize(MessageCodec codec, PacketMetaList incomingPackets, PacketMetaList outgoingPackets) {
        registerMessageDecoders(codec, incomingPackets);
        registerMessageEncoders(codec, outgoingPackets);
    }

    /**
     * Registers all of the message decoders.
     *
     * @param codec The message codec.
     * @param incomingPackets The incoming packets.
     */
    private void registerMessageDecoders(MessageCodec codec, PacketMetaList incomingPackets) {
        codec.registerMessageDecoder(incomingPackets.getId("heartbeat"), this::decodeHeartbeatMessage);
        codec.registerMessageDecoder(incomingPackets.getId("screen_info"), this::decodeScreenInfoMessage);
        codec.registerMessageDecoder(incomingPackets.getId("interfaces_closed"), this::decodeClosedInterfacesMessage);
        codec.registerMessageDecoder(incomingPackets.getId("chat"), this::decodeChatMessage);
        codec.registerMessageDecoder(incomingPackets.getId("command"), this::decodeCommandMessage);
        codec.registerMessageDecoder(incomingPackets.getId("switch_items"), this::decodeSwapItemMessage);

        for(int i = 1; i <= 1; i++) {
            final int optionId = i;                                                                     // Err...k
            codec.registerMessageDecoder(incomingPackets.getId("button_option_" + i), (packet) ->
                    decodeButtonOptionMessage(packet, optionId));
        }

        codec.registerMessageDecoder(incomingPackets.getId("item_option_1"), this::decodeItemOptionOneMessage);
        codec.registerMessageDecoder(incomingPackets.getId("inter_item_option_1"), this::decodeInterfaceItemOptionOneMessage);


        codec.registerMessageDecoder(incomingPackets.getId("route_ground"), (packet) -> decodeRouteMessage(packet, false));
        codec.registerMessageDecoder(incomingPackets.getId("route_target"), (packet) -> decodeRouteMessage(packet, false));
        codec.registerMessageDecoder(incomingPackets.getId("route_minimap"), (packet) -> decodeRouteMessage(packet, true));

        codec.registerMessageDecoder(incomingPackets.getId("rebuilt_scene"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("load_scene"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("camera_moved"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("packet_check"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("focus_changed"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("click"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("loc_option_1"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("settings"), this::decodeBlankMessage);
        codec.registerMessageDecoder(incomingPackets.getId("obj_option_unk"), this::decodeBlankMessage);
    }

    /**
     * TEMPORARY, just converts the packet into a blank message.
     *
     * @param packet The packet to decode.
     * @return The decoded message.
     */
    private BlankMessage decodeBlankMessage(Packet packet) {
        logger.info("No message decoder registered for packet: " + packet.getMeta().getName());
        return new BlankMessage();
    }

    /**
     * Decodes a heartbeat message.
     *
     * @param packet The packet to decode.
     * @return The decoded message.
     */
    private HeartbeatMessage decodeHeartbeatMessage(Packet packet) {
        return new HeartbeatMessage();
    }

    /**
     *
     * @param packet
     * @return
     */
    private ScreenInfoMessage decodeScreenInfoMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();
        int displayMode = buf.readUnsignedByte();
        int width = buf.readUnsignedShort();
        int height = buf.readUnsignedShort();
        int numberSamples = buf.readUnsignedByte();
        return new ScreenInfoMessage(displayMode, width, height, numberSamples);
    }

    /**
     *
     * @param packet
     * @param option
     * @return
     */
    public ButtonOptionMessage decodeButtonOptionMessage(Packet packet, int option) {
        ByteBuf buf = packet.getBuffer();
        int widgetId = buf.readUnsignedShort();
        int componentId = buf.readUnsignedShort();
        int childId = buf.readUnsignedShort();
        if(childId == 65535) {
            childId = -1;
        }
        return new ButtonOptionMessage(widgetId, componentId, childId, option);
    }

    /**
     *
     * @param packet
     * @return
     */
    private ClosedInterfacesMessage decodeClosedInterfacesMessage(Packet packet) {
        return new ClosedInterfacesMessage();
    }

    /**
     * Decodes a route message.
     *
     * @param packet The packet to decode.
     * @return The decoded message.
     */
    private RouteMessage decodeRouteMessage(Packet packet, boolean minimap) {
        ByteBuf buffer = packet.getBuffer();

        int steps = (buffer.readableBytes() - 5 - (minimap ? 14 : 0)) / 2;

        boolean active = readUByteA(buffer) == 1;
        int firstX = buffer.readUnsignedShort();
        int firstY = readShortA(buffer);

        RouteMessage message = new RouteMessage();
        message.appendPoint(firstX, firstY);

        while(steps-- > 0) {
            int dx = readByteA(buffer);
            int dy = readByteS(buffer);

            message.appendPoint(firstX + dx, firstY + dy);
        }
        return message;
    }

    /**
     * Decodes a submitPublicChat message.
     *
     * @param packet the packet to decode.
     * @return the decoded message.
     */
    private ChatMessage decodeChatMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();
        int color = buf.readUnsignedByte();
        int effect = buf.readUnsignedByte();

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        return new ChatMessage(ChatMessage.HUFFMAN_ENCODING, color, effect, data);
    }

    /**
     *
     * @param packet
     * @return
     */
    private CommandMessage decodeCommandMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();

        String str = readString(buf);
        String[] split = str.split(" ");

        String name = split.length > 0 ? split[0] : "";

        int argumentCount = Math.max(1, split.length);
        String[] args = Arrays.copyOfRange(split, 1, argumentCount);

        return new CommandMessage(name, args);
    }

    private SwapItemMessage decodeSwapItemMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();

        int firstSlot = buf.readUnsignedShort();

        int id = readLEInt(buf);
        int widgetId = id >> 16;
        int componentId = id & 0xffff;

        int secondSlot = readShortA(buf);
        int mode = readByteS(buf);

        return new SwapItemMessage(widgetId, componentId, firstSlot, secondSlot, mode);
    }

    private ItemOptionMessage decodeItemOptionOneMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();

        int itemId = readLEShort(buf);
        int slot = readShortA(buf);
        int id = readMEInt(buf);

        int widgetId = id >> 16;
        int componentId = id & 0xffff;
        return new ItemOptionMessage(widgetId, componentId, itemId, slot, 1);
    }

    private InterfaceItemOptionMessage decodeInterfaceItemOptionOneMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();

        int itemId = readShortA(buf);
        int slot = buf.readUnsignedShort();
        int id = readMEInt(buf);

        int widgetId = id >> 16;
        int componentId = id & 0xffff;
        return new InterfaceItemOptionMessage(widgetId, componentId, itemId, slot, 1);
    }

    /**
     *
     * @param packet
     * @return
     */
    private LocaleOptionMessage decodeLocaleOptionOneMessage(Packet packet) {
        ByteBuf buf = packet.getBuffer();

        int ukn = readLEShort(buf);
        int a = readShortA(buf);
        int b = buf.readUnsignedShort();

        return null;
    }


    /**
     * Registers all of the message encoders.
     *
     * @param codec The message codec.
     * @param outgoingPackets The outgoing packets.
     */
    private void registerMessageEncoders(MessageCodec codec, PacketMetaList outgoingPackets) {
        codec.registerMessageEncoder(RebuildSceneMessage.class, this::encodeRebuildSceneMessage);
        codec.registerMessageEncoder(SetRootInterfaceMessage.class, this::encodeSetRootInterfaceMessage);
        codec.registerMessageEncoder(SetInterfaceMessage.class, this::encodeSetInterfaceMessage);
        codec.registerMessageEncoder(PrintMessage.class, this::encodePrintMessage);
        codec.registerMessageEncoder(SetInterfaceHiddenMessage.class, this::encodeSetInterfaceHiddenMessage);
        codec.registerMessageEncoder(RefreshInventoryMessage.class, this::encodeRefreshInventoryMessage);
        codec.registerMessageEncoder(UpdateInventoryMessage.class, this::encodeUpdateInventoryMessage);
        codec.registerMessageEncoder(SetVariableMessage.class, this::encodeSetVariableMessage);
        codec.registerMessageEncoder(AccessOptionMessage.class, this::encodeAccessOptionsMessage);
        codec.registerMessageEncoder(PlaySongMessage.class, this::encodeSongMessage);
        codec.registerMessageEncoder(SetInterfaceTextMessage.class, this::encodeSetInterfaceTextMessage);
        codec.registerMessageEncoder(UpdateSkillMessage.class, this::encodeUpdateSkillMessage);

        // Register the synchronization message encoders
        codec.registerMessageEncoder(PlayerSynchronizationMessage.class, new PlayerSynchronizationMessageEncoder());
        codec.registerMessageEncoder(NpcSynchronizationMessage.class, new NpcSynchronizationMessageEncoder());
    }

    /**
     * Encodes a rebuild scene message.
     *
     * @param alloc The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param message The message to encode.
     * @return The encoded packet.
     */
    private Packet encodeRebuildSceneMessage(ByteBufAllocator alloc, PacketMetaList outgoingPackets,
                                             RebuildSceneMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("rebuild_scene"), alloc);

        Position position = message.getPosition();

        builder.writeShortA(position.getLocalX());

        boolean force = true;
        int regionX = position.getRegionX();
        int regionY = position.getRegionY();

        if ((regionX == 48 || regionX == 49) && regionY == 48) {
            force = false;
        }

        if (regionX == 48 && regionY == 148) {
            force = false;
        }

        int offset = Scene.LENGTH >> 4;
        for (int mapX = ((position.getBlockX() - offset) / 8); mapX <= ((position.getBlockX() + offset) / 8); mapX++) {
            for (int mapY = ((position.getBlockY() - offset) / 8); mapY <= ((position.getBlockY() + offset) / 8); mapY++) {
                if (force || (mapY != 49 && mapY != 149 && mapY != 147 && mapX != 50 && (mapX != 49 || mapY != 47))) {
                    int[] keys = landscapeKeys.getKeys(mapX, mapY);
                    for (int i = 0; i < 4; i++) {
                        builder.writeIMEInt(keys[i]);
                    }
                }
            }
        }

        builder.writeByteS(position.getPlane());
        builder.writeShort(position.getBlockX());
        builder.writeShortA(position.getBlockY());
        builder.writeShortA(position.getLocalY());
        return builder.build();
    }

    /**
     * Encodes a set root nterface message.
     *
     * @param allocator The byte buffer allocator.
     * @param outgoingPackets The outgoing packets.
     * @param message The message to encode.
     * @return The encoded packet.
     */
    private Packet encodeSetRootInterfaceMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets,
                                                 SetRootInterfaceMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("set_root_interface"), allocator);
        builder.writeLEShortA(message.getWidgetId());

        // When this is set to 2, rebuilds the scene using the existing given data
        builder.writeByteA(0);                                                                      // ?

        builder.writeLEShortA(0);                                                                   // Packet counter
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeSetInterfaceMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, SetInterfaceMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("set_interface"), allocator);
        builder.writeByte(message.getType());
        builder.writeIMEInt(message.getTargetId() << 16 | message.getComponentId());
        builder.writeShortA(0);
        builder.writeShort(message.getWidgetId());
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodePrintMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, PrintMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("print"), allocator);
        builder.writeJstr(message.getText());
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeSetInterfaceHiddenMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, SetInterfaceHiddenMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("set_interface_hidden"), allocator);
        builder.writeByteN(message.isHidden() ? 1 : 0);
        builder.writeShort(0);
        builder.writeLEInt(message.getWidgetId() << 16 | message.getComponentId());
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeRefreshInventoryMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, RefreshInventoryMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("refresh_inventory"), allocator);
        builder.writeInt(message.getWidgetId() << 16 | message.getComponentId());
        builder.writeShort(message.getId());

        Inventory inventory = message.getInventory();
        builder.writeShort(inventory.capacity());
        for(int slot = 0; slot < inventory.capacity(); slot++) {
            Item item = inventory.get(slot);
            if(item != null) {
                if(item.getAmount() >= 255) {
                    builder.writeByteS(255);
                    builder.writeInt(item.getAmount());
                } else {
                    builder.writeByteS(item.getAmount());
                }
                builder.writeShort(item.getId() + 1);
            } else {
                builder.writeByteS(0);
                builder.writeShort(0);
            }
        }
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeUpdateInventoryMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, UpdateInventoryMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("update_inventory"), allocator);
        builder.writeInt(message.getWidgetId() << 16 | message.getComponentId());
        builder.writeShort(message.getId());

        Inventory inventory = message.getInventory();
        for(int slot : message.getSlots()) {
            builder.writeSmart(slot);

            Item item = inventory.get(slot);
            if(item == null) {
                builder.writeShort(0);
                continue;
            } else {
                builder.writeShort(item.getId() + 1);
                if(item.getAmount() >= 255) {
                    builder.writeByte(255);
                    builder.writeInt(item.getAmount());
                } else {
                    builder.writeByte(item.getAmount());
                }
            }
        }

        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeSetVariableMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, SetVariableMessage message) {
        if(message.getValue() >= -128 && message.getValue() < 128) {
            PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("variable_b"), allocator);
            builder.writeShortA(message.getId());
            builder.writeByteN(message.getValue());
            return builder.build();
        } else {
            PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("variable_i"), allocator);
            builder.writeInt(message.getValue());
            builder.writeShortA(message.getId());
            return builder.build();
        }
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeAccessOptionsMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, AccessOptionMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("access_options"), allocator);
        builder.writeLEShort(0);
        builder.writeLEShort(message.getEnd());
        builder.writeInt(message.getWidgetId() << 16 | message.getComponentId());
        builder.writeShortA(message.getStart());
        builder.writeMEInt(message.getFlags());
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeSongMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, PlaySongMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("song"), allocator);
        builder.writeLEShortA(message.getId());
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeSetInterfaceTextMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, SetInterfaceTextMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("interface_text"), allocator);
        builder.writeIMEInt(message.getWidgetId() << 16 | message.getComponentId());
        builder.writeJstr(message.getText());
        builder.writeShortA(0);
        return builder.build();
    }

    /**
     *
     * @param allocator
     * @param outgoingPackets
     * @param message
     * @return
     */
    private Packet encodeUpdateSkillMessage(ByteBufAllocator allocator, PacketMetaList outgoingPackets, UpdateSkillMessage message) {
        PacketBuilder builder = PacketBuilder.create(outgoingPackets.get("update_skill"), allocator);
        builder.writeByteA(message.getStat());
        builder.writeMEInt(message.getExperience());
        builder.writeByte(message.getId());
        return builder.build();
    }
}