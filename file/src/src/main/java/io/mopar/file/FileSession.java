package io.mopar.file;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Hadyn Fitzgerald
 */
public class FileSession {

    /**
     * The amount of requests to limit to session to.
     */
    public static final int REQUEST_LIMIT = 20;

    /**
     * The id.
     */
    private int id = -1;

    /**
     * The current chunk.
     */
    private int chunk;

    /**
     * The priority request queue.
     */
    private Queue<FileRequest> priorityRequests = new ArrayDeque<>();

    /**
     * The normal request queue.
     */
    private Queue<FileRequest> normalRequests = new ArrayDeque<>();

    /**
     * The online flag.
     */
    private boolean online;

    /**
     * The current request.
     */
    private FileRequest currentRequest;

    /**
     * Sets the id.
     *
     * @param id The id.
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Appends a new file request.
     *
     * @param request The file request.
     * @return If the request was accepted.
     */
    public boolean append(FileRequest request) {
        Queue<FileRequest> requests = request.isPriority() ? priorityRequests : normalRequests;
        if(requests.size() >= REQUEST_LIMIT) {
            return false;
        }
        requests.add(request);
        return true;
    }

    /**
     * Polls the next pending request.
     *
     * @return The request or <code>null</code> if the session has no pending requests.
     */
    public FileRequest poll() {
        Queue<FileRequest> requests = priorityRequests.isEmpty() ? normalRequests : priorityRequests;
        return requests.poll();
    }

    /**
     * Gets if the session has a pending request.
     *
     * @return The request.
     */
    public boolean hasPendingRequests() {
        return !priorityRequests.isEmpty() || !normalRequests.isEmpty();
    }

    /**
     * Sets the current request.
     *
     * @param request The current request.
     */
    public void setCurrentRequest(FileRequest request) {
        this.currentRequest = request;
    }

    /**
     * Gets the current request.
     *
     * @return The current request.
     */
    public FileRequest getCurrentRequest() {
        return currentRequest;
    }

    /**
     * Gets if the session has an active request.
     *
     * @return If the active request is <code>null</code> or if the active request is completed.
     */
    public boolean hasActiveRequest() {
        return currentRequest != null;
    }

    /**
     * Clears all the pending requests.
     */
    public void clear() {
        priorityRequests.clear();
    }
}