package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;
import io.mopar.rs2.net.Session;

/**
 * @author Hadyn Fitzgerald
 */
public interface MessageHandler<T extends Message> {

    /**
     * Handles a message.
     *
     * @param session The session the message was received from.
     * @param message The message to handle.
     */
    void handle(Session session, T message);
}
