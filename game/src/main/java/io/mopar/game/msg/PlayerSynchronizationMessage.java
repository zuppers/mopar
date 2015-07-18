package io.mopar.game.msg;

import io.mopar.core.msg.Message;
import io.mopar.game.model.*;
import io.mopar.game.model.MobileSceneGraph.Node;
import io.mopar.game.sync.player.AddPlayerDescriptor;
import io.mopar.game.sync.PlayerDescriptor;
import io.mopar.game.sync.player.RemovedPlayerDescriptor;
import io.mopar.game.sync.player.TeleportingPlayerDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class PlayerSynchronizationMessage extends Message {

    /**
     * The relative position.
     */
    private Position position;

    /**
     * The descriptor for the local player.
     */
    private PlayerDescriptor selfDescriptor;

    /**
     * The player descriptors.
     */
    private List<PlayerDescriptor> descriptors = new ArrayList<>();

    /**
     * The amount of local player descriptors.
     */
    private int localPlayerCount;

    /**
     * Constructs a new {@link PlayerSynchronizationMessage};
     *
     * @param player The local player.
     */
    public PlayerSynchronizationMessage(Player player) {
        if(!player.isTeleporting()) {
            selfDescriptor = PlayerDescriptor.create(player);
        } else {
            selfDescriptor = new TeleportingPlayerDescriptor(player, player.getScene().getPosition());
        }
        position = player.getPosition();
    }

    /**
     * Adds a descriptor.
     *
     * @param descriptor The descriptor.
     */
    public void addDescriptor(PlayerDescriptor descriptor) {
        descriptors.add(descriptor);
    }

    /**
     * Gets the self descriptor.
     *
     * @return The self descriptor.
     */
    public PlayerDescriptor getSelfDescriptor() {
        return selfDescriptor;
    }

    /**
     * Gets the player descriptors.
     *
     * @return The player descriptors.
     */
    public List<PlayerDescriptor> getDescriptors() {
        return descriptors;
    }

    /**
     * Sets the amount of local player descriptors.
     *
     * @param count The count.
     */
    public void setLocalPlayerCount(int count) {
        localPlayerCount = count;
    }

    /**
     * Gets the amount of local player descriptors.
     *
     * @return The local player count.
     */
    public int getLocalPlayerCount() {
        return localPlayerCount;
    }

    /**
     * Creates a new {@link PlayerSynchronizationMessage} using the provided players scene.
     *
     * @param player The local player.
     * @return The created message.
     */
    public static PlayerSynchronizationMessage create(Player player) {
        PlayerSynchronizationMessage message = new PlayerSynchronizationMessage(player);

        MobileSceneGraph<Player> scene = player.getScene().getLocalPlayers();
        int localCount = 0;
        for(Node<Player> node : scene.getNodes()) {
            if(node.getId() == player.getId()) {
                continue;
            }

            Player plr = node.getEntity();
            int state = scene.getState(node.getId());

            if(state != MobileSceneGraph.STATE_ADDED) {
                switch (state) {
                    case MobileSceneGraph.STATE_IDLE:
                        if(!scene.isVisible(node.getId())) {
                            continue;
                        }
                        message.addDescriptor(PlayerDescriptor.create(plr));
                        break;
                    case MobileSceneGraph.STATE_REMOVED:
                    case MobileSceneGraph.STATE_TELEPORTED:
                        message.addDescriptor(new RemovedPlayerDescriptor(plr));
                        break;
                }
                localCount++;
            }
        }

        for(Node<Player> node : scene.getNodes()) {
            if(node.getId() == player.getId()) {
                continue;
            }

            Player plr = node.getEntity();
            int state = scene.getState(node.getId());

            if(scene.isVisible(node.getId())) {
                switch (state) {
                    case MobileSceneGraph.STATE_ADDED:
                    case MobileSceneGraph.STATE_TELEPORTED:
                        message.addDescriptor(new AddPlayerDescriptor(plr, player.getPosition()));
                        break;
                }
            }
        }

        message.setLocalPlayerCount(localCount);

        return message;
    }

    public Position getPosition() {
        return position;
    }
}
