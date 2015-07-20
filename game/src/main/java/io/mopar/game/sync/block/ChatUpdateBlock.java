package io.mopar.game.sync.block;

import io.mopar.game.msg.ChatMessage;
import io.mopar.game.sync.UpdateBlock;

/**
 * @author Hadyn Fitzgerald
 */
public class ChatUpdateBlock extends UpdateBlock {

    /**
     * The chat message.
     */
    private ChatMessage message;

    /**
     * Constructs a new {@link ChatUpdateBlock};
     *
     * @param message the chat message.
     */
    public ChatUpdateBlock(ChatMessage message) {
        this.message = message;
    }

    /**
     * Gets the chat message.
     *
     * @return the message.
     */
    public ChatMessage getMessage() {
        return message;
    }
}
