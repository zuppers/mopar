package io.mopar.rs2;

import io.mopar.core.Service;
import io.mopar.core.msg.Message;
import io.mopar.rs2.msg.MessageDispatcher;
import io.mopar.rs2.msg.MessageHandler;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class ApplicationService<T extends Service> {

    /**
     * The service.
     */
    protected T service;

    /**
     * The dispatcher.
     */
    protected MessageDispatcher dispatcher = new MessageDispatcher();

    /**
     * Constructs a new {@link ApplicationService};
     *
     * @param service The service.
     */
    protected ApplicationService(T service) {
        this.service = service;
    }

    /**
     * Initializes the application to support the service.
     *
     * @param app The application.
     */
    public abstract void setup(Application app);

    /**
     * Starts the service.
     *
     * @param app The application.
     */
    public void start(Application app) {
        setup(app);
        service.start();
    }

    /**
     * Registers a message handler.
     *
     * @param type The message type.
     * @param handler The message handler.
     * @param <S> The generic message type.
     */
    public <S extends Message> void registerMessageHandler(Class<S> type, MessageHandler<S> handler) {
        dispatcher.registerHandler(type, handler);
    }

    /**
     * Gets the service.
     *
     * @return The service.
     */
    public T getService() {
        return service;
    }
}
