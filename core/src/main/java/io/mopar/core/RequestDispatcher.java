package io.mopar.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Hadyn Fitzgerald
 *
 * This implementation is thread safe for submitting messages while dispatching.
 */
public class RequestDispatcher {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);

    /**
     * The requestuest handlers mapped by their type.
     */
    private Map<Class<? extends Request>, RequestHandler<?, ?>> handlers = new HashMap<>();

    /**
     * The pending requestuests.
     */
    private Queue<PendingRequest> pendingRequests = new ArrayDeque<>();

    /**
     * The flag for if new requests should be rejected.
     */
    private boolean rejectRequests;

    /**
     * Dispatches the requests.
     */
    public void dispatch() {
        PendingRequest pendingRequest;
        while(true) {
            synchronized (pendingRequests) {
                pendingRequest = pendingRequests.poll();
                if(pendingRequest == null) {
                    break;
                }
            }

            try {
                handle(pendingRequest);
            } catch (Throwable t) {
                // TODO(sinisoul): How to report this to a callback?
                logger.error("Unhandled exception caught while handling request, {}",
                        pendingRequest.getRequestType().getSimpleName(), t);
                continue;
            }

            // Check that the request was answered
            if(!pendingRequest.isAnswered()) {
                logger.warn("Request as not answered after handling, {}", pendingRequest.getRequestType().getSimpleName());
            }
        }
    }

    /**
     * Sets if requests should be rejected.
     *
     * @param rejectRequests The reject requests flag.
     */
    public void setRejectRequests(boolean rejectRequests) {
        synchronized (pendingRequests) {
            this.rejectRequests = rejectRequests;
        }
    }

    /**
     * Handles a pending request.
     *
     * @param pendingRequest The pending request.
     */
    private void handle(PendingRequest pendingRequest) {
        RequestHandler handler = handlers.get(pendingRequest.getRequestType());
        handler.handle(pendingRequest.getRequest(), pendingRequest.getCallback());
    }

    /**
     * Submits a new request.
     *
     * @param requestuest The request to submit.
     * @param callback The request callback.
     * @return If the request was successfully submitted.
     */
    public boolean submit(Request requestuest, Callback callback) {
        synchronized (pendingRequests) {
            if(rejectRequests) {
                return false;
            }
            pendingRequests.add(new PendingRequest(requestuest, callback));
            return true;
        }
    }

    /**
     * Binds a new request handler.
     *
     * @param type The request type.
     * @param handler The request handler.
     * @param <T> The generic request type.
     */
    public <T extends Request> void bind(Class<T> type, RequestHandler<T, ?> handler) {
        handlers.put(type, handler);
    }

    /**
     *
     * @return
     */
    public boolean empty() {
        synchronized (pendingRequests) {
            return pendingRequests.isEmpty();
        }
    }
}