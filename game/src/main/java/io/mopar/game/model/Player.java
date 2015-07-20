package io.mopar.game.model;

import io.mopar.core.msg.Message;
import io.mopar.core.msg.MessageListener;
import io.mopar.game.msg.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class Player extends Mobile {

    /**
     * The message listeners.
     */
    private List<MessageListener> messageListeners = new ArrayList<>();

    /**
     * The display mode.
     */
    private int displayMode;

    /**
     * The scene.
     */
    private Scene scene = new Scene();

    /**
     * The chat message.
     */
    private ChatMessage chatMessage;

    /**
     * Constructs a new {@link Player};
     */
    public Player() {}

    /**
     * Sets the display mode.
     *
     * @param displayMode The display mode.
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
     *
     * @return
     */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
     *
     * @param message
     */
    public void setChatMessage(ChatMessage message) {
        this.chatMessage = message;
        setUpdated(true);
    }

    /**
     *
     * @return
     */
    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    /**
     *
     * @return
     */
    public boolean hasChatMessage() {
        return chatMessage != null;
    }

    /**
     *
     * @param widgetId
     */
    public void setRootInterface(int widgetId) {
        send(new SetRootInterfaceMessage(widgetId));
    }

    /**
     *
     * @param parentId
     * @param componentId
     * @param widgetId
     * @param type
     */
    public void setInterface(int parentId, int componentId, int widgetId, int type) {
        send(new SetInterfaceMessage(parentId, componentId, widgetId, type));
    }

    /**
     * Gets the scene.
     *
     * @return the scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Updates the local player graph.
     *
     * @param players The player list.
     */
    public void updateLocalPlayers(EntityList<Player> players) {
        scene.updateLocalPlayers(players, getPosition());
        send(PlayerSynchronizationMessage.create(this));
    }

    /**
     * Gets if the scene needs to be built using the current position
     * to check the condition.
     *
     * @return if the scene needs to be built.
     */
    public boolean getRebuildScene() {
        return scene.checkRebuild(getPosition());
    }

    /**
     * Builds the scene at the current position.
     */
    public void rebuildScene() {
        scene.build(getPosition());
        send(new RebuildSceneMessage(getPosition()));
    }

    /**
     * Prints text to the players console.
     *
     * @param text
     */
    public void print(String text) {
        send(new PrintMessage(text));
    }

    /**
     * Adds a message listener.
     *
     * @param listener The message listener.
     */
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     * Sends a message.
     *
     * @param message The message to send.
     */
    public void send(Message message) {
        messageListeners.forEach(listener -> listener.handle(message));
    }

    /**
     * Resets the player.
     */
    public void reset() {
        chatMessage = null;
        super.reset();
    }
}
