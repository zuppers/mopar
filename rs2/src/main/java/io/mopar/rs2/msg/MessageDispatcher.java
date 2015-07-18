package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;
import io.mopar.rs2.net.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class MessageDispatcher {

    /**
     * The handlers.
     */
    private Map<Class<? extends Message>, MessageHandler<?>> handlers = new HashMap<>();

    /**
     * Constructs a new {@link MessageDispatcher};
     */
    public MessageDispatcher() {}

    /**
     * Registers a handler.
     *
     * @param type The message type to bind the handler for.
     * @param handler The handler.
     * @param <T> The generic message type.
     */
    public <T extends Message> void registerHandler(Class<T> type, MessageHandler<T> handler) {
        handlers.put(type, handler);
    }

    /**
     * Dispatches a message.
     *
     * @param session The session the message was received from.
     * @param message The message.
     * @param <T> The generic message type.
     * @return If the message was handled or <code>false</code> if there is no handler for the specified
     *          message type.
     */
    public <T extends Message> boolean dispatch(Session session, T message) {
        MessageHandler<T> handler = (MessageHandler<T>) handlers.get(message.getClass());
        if(handler != null) {
            handler.handle(session, message);
            return true;
        }
        return false;
    }
}
