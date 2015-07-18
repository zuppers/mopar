package io.mopar.core;

/**
 * @author Hadyn Fitzgerald
 */
public class PendingRequest<T extends Request, U extends Response> {

    /**
     * The req.
     */
    private T request;

    /**
     * The response callback.
     */
    private Callback<U> callback;

    /**
     * Flag for if the req was answered.
     */
    private boolean answered;

    /**
     * Constructs a new {@link PendingRequest};
     *
     * @param request The req.
     * @param callback The req callback.
     */
    public PendingRequest(T request, Callback<U> callback) {
        this.request = request;

        // Wrap the callback to assure that the request was answered
        this.callback = (req) -> {
            callback.call(req);
            answered = true;
        };
    }

    /**
     * Gets the req.
     *
     * @return The req.
     */
    public T getRequest() {
        return request;
    }

    /**
     * Gets the callback.
     *
     * @return The callback.
     */
    public Callback<U> getCallback() {
        return callback;
    }

    /**
     * Gets if the req was answered.
     *
     * @return If the req was answered.
     */
    public boolean isAnswered() {
        return answered;
    }

    /**
     * Helper method; gets the req type derived from its class.
     *
     * @return The req class.
     */
    public Class<? extends Request> getRequestType() {
        return request.getClass();
    }
}