package io.mopar.game.sync.block;

import io.mopar.game.msg.ChatMessage;
import io.mopar.game.sync.UpdateBlock;

/**
 * @author Hadyn Fitzgerald
 */
public class ChatUpdateBlock extends UpdateBlock {

    /**
     * The submitPublicChat message.
     */
    private ChatMessage message;

    /**
     * Constructs a new {@link ChatUpdateBlock};
     *
     * @param message the submitPublicChat message.
     */
    public ChatUpdateBlock(ChatMessage message) {
        this.message = message;
    }

    /**
     * Gets the submitPublicChat message.
     *
     * @return the message.
     */
    public ChatMessage getMessage() {
        return message;
    }
}
