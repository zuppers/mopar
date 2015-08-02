package io.mopar.game.msg;

import io.mopar.core.msg.Message;
import io.mopar.game.model.MobileSceneGraph;
import io.mopar.game.model.MobileSceneGraph.Node;
import io.mopar.game.model.NPC;
import io.mopar.game.model.Player;
import io.mopar.game.model.Position;
import io.mopar.game.sync.NpcDescriptor;
import io.mopar.game.sync.npc.AddNpcDescriptor;
import io.mopar.game.sync.npc.RemovedNpcDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadyn on 7/28/2015.
 */
public class NpcSynchronizationMessage extends Message {

    /**
     * The relative position.
     */
    private Position position;

    /**
     * The player descriptors.
     */
    private List<NpcDescriptor> descriptors = new ArrayList<>();

    /**
     * The amount of local player descriptors.
     */
    private int localPlayerCount;

    /**
     * Constructs a new {@link PlayerSynchronizationMessage};
     *
     * @param player The local player.
     */
    public NpcSynchronizationMessage(Player player) {
        position = player.getPosition();
    }

    /**
     * Adds a descriptor.
     *
     * @param descriptor The descriptor.
     */
    public void addDescriptor(NpcDescriptor descriptor) {
        descriptors.add(descriptor);
    }

    /**
     * Gets the player descriptors.
     *
     * @return The player descriptors.
     */
    public List<NpcDescriptor> getDescriptors() {
        return descriptors;
    }

    /**
     * Sets the amount of local player descriptors.
     *
     * @param count The count.
     */
    public void setLocalNpcCount(int count) {
        localPlayerCount = count;
    }

    /**
     * Gets the amount of local player descriptors.
     *
     * @return The local player count.
     */
    public int getLocalNpcCount() {
        return localPlayerCount;
    }

    /**
     *
     * @return
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Creates a new {@link NpcSynchronizationMessage} using the provided players scene.
     *
     * @param player The local player.
     * @return The created message.
     */
    public static NpcSynchronizationMessage create(Player player) {
        NpcSynchronizationMessage message = new NpcSynchronizationMessage(player);

        MobileSceneGraph<NPC> scene = player.getScene().getLocalNpcs();
        int localCount = 0;
        for(Node<NPC> node : scene.getNodes()) {
            NPC plr = node.getEntity();
            int state = scene.getState(node.getId());

            if(state != MobileSceneGraph.STATE_ADDED) {
                switch (state) {
                    case MobileSceneGraph.STATE_IDLE:
                        if(!scene.isVisible(node.getId())) {
                            continue;
                        }
                        message.addDescriptor(NpcDescriptor.create(plr));
                        break;
                    case MobileSceneGraph.STATE_REMOVED:
                    case MobileSceneGraph.STATE_TELEPORTED:
                        message.addDescriptor(new RemovedNpcDescriptor(plr));
                        break;
                }
                localCount++;
            }
        }

        for(Node<NPC> node : scene.getNodes()) {
            NPC npc = node.getEntity();
            int state = scene.getState(node.getId());

            if(scene.isVisible(node.getId())) {
                switch (state) {
                    case MobileSceneGraph.STATE_ADDED:
                    case MobileSceneGraph.STATE_TELEPORTED:
                        message.addDescriptor(new AddNpcDescriptor(npc, player.getPosition()));
                        break;
                }
            }
        }

        message.setLocalNpcCount(localCount);

        return message;
    }
}