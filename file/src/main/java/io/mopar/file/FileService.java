package io.mopar.file;

import io.mopar.core.Callback;
import io.mopar.core.Service;
import io.mopar.file.req.CreateSessionRequest;
import io.mopar.file.req.RemoveSessionRequest;
import io.mopar.file.req.StreamFileRequest;
import io.mopar.file.res.CreateSessionResponse;
import io.mopar.file.res.RemoveSessionResponse;
import io.mopar.file.res.StreamFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Hadyn Fitzgerald
 */
public class FileService extends Service {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    /**
     * The maximum amount of sessions allowed.
     */
    public static final int MAXIMUM_SESSIONS = 2000;

    /**
     * Default throughput of four megabytes per second.
     */
    public static final int DEFAULT_THROUGHPUT = 4194304;

    /**
     * The sessions.
     */
    private FileSessionList sessions = new FileSessionList(MAXIMUM_SESSIONS);

    /**
     * The file provider.
     */
    private FileProvider fileProvider;

    /**
     * The amount of chunks to write per cycle.
     */
    private int chunksPerCycle;

    /**
     * The delay between cycles in milliseconds.
     */
    private long delay = 50;

    /**
     * The current cursor to process sessions form.
     */
    private int cursor = -1;

    /**
     * Constructs a new {@link FileService};
     */
    public FileService() {
        setThroughput(DEFAULT_THROUGHPUT);
    }

    @Override
    public void setup() throws Exception {
        registerRequestHandler(CreateSessionRequest.class, this::handleCreateSessionRequest);
        registerRequestHandler(RemoveSessionRequest.class, this::handleRemoveSessionRequest);
        registerRequestHandler(StreamFileRequest.class, this::handleStreamFileRequest);
    }

    @Override
    public void pulse() throws Exception {
        long start = System.currentTimeMillis();

        int writtenChunks = 0;
        int inactiveRounds = 0;
        while(writtenChunks < chunksPerCycle) {
            boolean active = false;
            int count = 0;
            if(!sessions.empty()) {
                for (int i = 0; i < sessions.capacity(); i++) {
                    cursor = (cursor + 1) % sessions.capacity();

                    if (!sessions.contains(cursor)) {
                        continue;
                    }

                    FileSession session = sessions.get(cursor);
                    count++;

                    // If the session does not have an active request, try and see if there are any and append it
                    if (!session.hasActiveRequest()) {
                        if (!session.hasPendingRequests()) {
                            continue;
                        }
                        session.setCurrentRequest(session.poll());
                    }

                    try {
                        FileRequest request = session.getCurrentRequest();

                        // Get the length of the file in chunks
                        int length = fileProvider.chunkedLength(request.getVolumeId(), request.getFileId());

                        // Write out the next chunk for the request
                        request.write(fileProvider.getChunk(request.getVolumeId(), request.getFileId(), request.incrementChunk(), request.isPriority()));
                        writtenChunks++;

                        if (request.getChunk() >= length) {
                            session.setCurrentRequest(null);
                            request.close();
                        } else {
                            active = true;
                        }

                        if (count >= sessions.size()) {
                            break;
                        }
                    } catch (Throwable t) {
                        FileRequest current = session.getCurrentRequest();
                        logger.error("Issue with serving file " + current.getVolumeId() + ", " + current.getFileId(), t);
                        session.setCurrentRequest(null);
                        continue;
                    }
                }
            }

            // Stop writing chunks if we pass the mark for inactive rounds
            if(!active && inactiveRounds++ > 40) {
                break;
            }

            // Manually dispatch requests to see if any new ones to process came in
            dispatchRequests();
        }

        long elapsed = System.currentTimeMillis() - start;

        try {

            long delta = delay - elapsed;
            if(delta > 0) {
                Thread.sleep(delta);
            }
        } catch (InterruptedException ex) {}
    }

    @Override
    public void teardown() {}

    /**
     * Sets the file provider.
     *
     * @param fileProvider The file provider.
     */
    public void setFileProvider(FileProvider fileProvider) {
        this.fileProvider = fileProvider;
    }

    /**
     * Sets the output throughput.
     *
     * @param bytesPerSecond The maximum amount of bytes to write per second.
     */
    public void setThroughput(int bytesPerSecond) {
        chunksPerCycle = (int) ((bytesPerSecond / FileChunk.LENGTH) / (1000 / delay));
    }

    /**
     * Creates a new session.
     *
     * @param callback The response callback.
     */
    public void createSession(Callback<CreateSessionResponse> callback) {
        submit(new CreateSessionRequest(), callback);
    }

    /**
     * Removes a session.
     *
     * @param id The session id.
     */
    public void removeSession(int id, Callback<RemoveSessionResponse> callback) {
        submit(new RemoveSessionRequest(id), callback);
    }

    /**
     * Streams a file.
     *
     * @param sessionId The session id.
     * @param volumeId The volume id for the file to request.
     * @param fileId The id for the file to request.
     * @param priority Flag for if the request is a priority.
     */
    public void stream(int sessionId, int volumeId, int fileId, boolean priority, FileChunkStream stream, Callback<StreamFileResponse> callback) {
        submit(new StreamFileRequest(sessionId, volumeId, fileId, priority, stream), callback);
    }

    /**
     * Handles a request to create a new session.
     *
     * @param request The request.
     * @param callback The response callback.
     */
    private void handleCreateSessionRequest(CreateSessionRequest request, Callback callback) {
        FileSession session = new FileSession();

        // Attempt to add the session, if that fails alert the service is full
        if(!sessions.add(session)) {
            callback.call(new CreateSessionResponse(CreateSessionResponse.FULL));
            return;
        }

        // Write back that request was successful
        CreateSessionResponse response = new CreateSessionResponse(CreateSessionResponse.OK);
        response.setSession(session);
        callback.call(response);
    }

    /**
     * Handles a request to remove a session.
     *
     * @param request The request.
     * @param callback The response callback.
     */
    private void handleRemoveSessionRequest(RemoveSessionRequest request, Callback callback) {
        if(!sessions.exists(request.getSessionId())) {
            callback.call(new RemoveSessionResponse(RemoveSessionResponse.SESSION_DOES_NOT_EXIST));
            return;
        }

        sessions.remove(request.getSessionId());

        callback.call(new RemoveSessionResponse(RemoveSessionResponse.OK));
    }

    /**
     * Handles a request to stream a file.
     *
     * @param request The request.
     * @param callback The response callback.
     */
    private void handleStreamFileRequest(StreamFileRequest request, Callback callback) {

        // Get the session that is request a new file stream, if the session does not exist write a failed request response
        FileSession session = sessions.get(request.getSessionId());
        if(session == null) {
            callback.call(new StreamFileResponse(StreamFileResponse.SESSION_DOES_NOT_EXIST));
            return;
        }

        // Check if the requested file exists
        if (!fileProvider.exists(request.getVolumeId(), request.getFileId())) {
            callback.call(new StreamFileResponse(StreamFileResponse.FILE_DOES_NOT_EXIST));
            return;
        }

        // Append the request to the session request queue, if the session does not accept the request write back that
        // the request limit was reached.
        if(!session.append(new FileRequest(request.getVolumeId(), request.getFileId(), request.isPriority(), request.getStream()))) {
            callback.call(new StreamFileResponse(StreamFileResponse.REACHED_REQUEST_LIMIT));
            return;
        }

        callback.call(new StreamFileResponse(StreamFileResponse.OK));
    }

}
