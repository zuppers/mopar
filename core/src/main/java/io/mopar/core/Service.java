package io.mopar.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class Service implements Runnable {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    /**
     * The executor.
     */
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * The request dispatcher.
     */
    private RequestDispatcher dispatcher = new RequestDispatcher();

    /**
     * The flag for if the service thread should block until requests are received.
     */
    private boolean blockForRequests = false;

    /**
     * The flag for if a stop was requested.
     */
    private boolean stopRequested = false;

    /**
     * The flag for if the service has been started.
     */
    private boolean started = false;

    /**
     * Constructs a new {@link Service};
     */
    protected Service() {}

    /**
     * Initializes the service.
     */
    public abstract void setup() throws Exception;

    /**
     * Calls the service logic.
     */
    public abstract void pulse() throws Exception;

    /**
     * Destroys the service.
     */
    public abstract void teardown();

    @Override
    public void run() {

        // Initialize the service
        try {
            setup();
        } catch (Exception ex) {
            logger.error("Uncaught exception encountered while setting up service", ex);
            stopRequested = true;
        }

        for(;;) {
            if(stopRequested) {
                break;
            }

            synchronized (this) {
                if (blockForRequests && dispatcher.empty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
            }

            dispatchRequests();

            try {
                pulse();
            } catch (Throwable ex) {
                logger.info("Uncaught exception encountered while updating service", ex);
            }
        }

        // Clean up after shutdown, reject any new requests for the service
        dispatcher.setRejectRequests(true);
        teardown();
    }

    /**
     * Sets if the service should block for requests.
     *
     * @param blockForRequests The block for requests flag.
     */
    public void setBlockForRequests(boolean blockForRequests) {
        this.blockForRequests = blockForRequests;
    }

    /**
     * Registers a request handler.
     *
     * @param type The request type.
     * @param handler The request handler.
     * @param <T> The generic request type.
     */
    public <T extends Request> void registerRequestHandler(Class<T> type, RequestHandler<T, ?> handler) {
        dispatcher.bind(type, handler);
    }

    /**
     * Submits a new request to the service.
     *
     * @param request The request.
     * @param callback The callback.
     */
    public void submit(Request request, Callback callback) {
        synchronized (this) {
            dispatcher.submit(request, callback);
            notifyAll();
        }
    }

    /**
     * Manually dispatches the requests.
     */
    public void dispatchRequests() {
        dispatcher.dispatch();
    }

    /**
     * Starts the service.
     */
    public void start() {
        if(started) {
            throw new IllegalStateException("Service has already been started");
        }

        // Submit this service to the executor
        executor.submit(this);
        started = true;
    }

    /**
     * Shuts down the service gracefully.
     */
    public void shutdown() {
        synchronized (this) {
            stopRequested = true;
            notifyAll();
        }
        executor.shutdown();
        while(!executor.isTerminated()) {
            try {
                executor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {}
        }
    }
}
