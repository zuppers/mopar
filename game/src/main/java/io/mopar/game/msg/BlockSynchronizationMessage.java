package io.mopar.game.msg;

import com.google.common.collect.Sets;
import io.mopar.game.model.*;
import io.mopar.game.model.block.*;

import java.util.*;

import static io.mopar.game.model.BlockSceneGraph.*;

/**
 * @author Hadyn Fitzgerald
 */
public class BlockSynchronizationMessage extends BlockMessage {

    interface BlockEventEncoder<T extends BlockEvent> {
        BlockMessage encode(T event);
    }

    /**
     *
     */
    private static Set<Class<? extends BlockEvent>> rebuildEvents = Sets.newHashSet(GameObjectUpdatedEvent.class, GameObjectRemovedEvent.class);

    /**
     * The encoders.
     */
    private static Map<Class<? extends BlockEvent>, BlockEventEncoder> encoders = new HashMap<>();

    /**
     *
     */
    private int x;

    /**
     *
     */
    private int y;

    /**
     * The messages.
     */
    private List<BlockMessage> messages = new ArrayList<>();

    /**
     * Constructs a new {@link BlockSynchronizationMessage};
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    private BlockSynchronizationMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param message
     */
    public void add(BlockMessage message) {
        messages.add(message);
    }

    /**
     *
     * @param player
     * @return
     */
    public static List<BlockMessage> create(Player player, RegionSet regions) {
        BlockSceneGraph graph = player.getScene().getBlockGraph();
        if(graph.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<BlockMessage> messages = new ArrayList<>();
        for(Node node : graph.getNodes()) {
            Block block = regions.getBlock(node.getPlane(), node.getBlockX(), node.getBlockY());
            int state = graph.getState(node.getX(), node.getY());
            switch (state) {
                case BlockSceneGraph.REBUILD:
                    messages.add(new ResetBlockMessage(node.getX(), node.getY()));
                    for(int hash : block.getUpdatedObjectTiles()) {
                        int group = hash >> 12;
                        int x = hash >> 6 & 0x3f;
                        int y = hash & 0x3f;

                        GameObject gameObject = block.getGameObject(x, y, group);
                        if(gameObject.isRemoved()) {
                            messages.add(new RemoveGameObjectMessage(x, y, gameObject.getType(), gameObject.getOrientation()));
                        } else {
                            messages.add(new UpdateGameObjectMessage(x, y, gameObject.getType(), gameObject.getConfigId(), gameObject.getOrientation()));
                        }
                    }
                case BlockSceneGraph.UPDATED:
                    BlockSynchronizationMessage sync = new BlockSynchronizationMessage(node.getX(), node.getY());
                    boolean bool = false;
                    for(BlockEvent event : block.getEvents()) {
                        if(state == BlockSceneGraph.REBUILD && rebuildEvents.contains(event.getClass())) {
                            continue;
                        }
                        sync.add(encodeEvent(event));
                        bool = true;
                    }
                    if(bool) {
                        messages.add(sync);
                    }
                    break;
            }
        }
        return messages;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return
     */
    public List<BlockMessage> getMessages() {
        return messages;
    }

    /**
     *
     * @param event
     * @return
     */
    private static BlockMessage encodeEvent(BlockEvent event) {
        BlockEventEncoder encoder = encoders.get(event.getClass());
        if(encoder == null) {
            throw new IllegalStateException("No registered encoder for block event: " + event.getClass().getSimpleName());
        }
        return encoder.encode(event);
    }

    /**
     *
     * @param eventClass
     * @param encoder
     * @param <T>
     */
    private static <T extends BlockEvent> void registerEncoder(Class<T> eventClass, BlockEventEncoder<T> encoder) {
        encoders.put(eventClass, encoder);
    }

    /**
     *
     * @param event
     * @return
     */
    private static CreateStillGraphicMessage encodeStillGraphicBlockEvent(StillGraphicCreatedBlockEvent event) {
        StillGraphic stillGraphic = event.getStillGraphic();
        Position position = stillGraphic.getPosition();
        return new CreateStillGraphicMessage(position.getX() & 7, position.getY() & 7, stillGraphic.getGraphic());
    }

    /**
     *
     * @param event
     * @return
     */
    private static UpdateGameObjectMessage encodeGameObjectUpdatedEvent(GameObjectUpdatedEvent event) {
        GameObject obj = event.getGameObject();
        Position position = obj.getPosition();
        return new UpdateGameObjectMessage(position.getX() & 7, position.getY() & 7, obj.getType(), obj.getConfigId(), obj.getOrientation());
    }

    /**
     *
     * @param event
     * @return
     */
    private static RemoveGameObjectMessage encodeGameObjectRemovedEvent(GameObjectRemovedEvent event) {
        GameObject obj = event.getGameObject();
        Position position = obj.getPosition();
        return new RemoveGameObjectMessage(position.getX() & 7, position.getY() & 7, obj.getType(), obj.getOrientation());
    }


    static {
        registerEncoder(StillGraphicCreatedBlockEvent.class, BlockSynchronizationMessage::encodeStillGraphicBlockEvent);
        registerEncoder(GameObjectUpdatedEvent.class, BlockSynchronizationMessage::encodeGameObjectUpdatedEvent);
        registerEncoder(GameObjectRemovedEvent.class, BlockSynchronizationMessage::encodeGameObjectRemovedEvent);
    }
}
