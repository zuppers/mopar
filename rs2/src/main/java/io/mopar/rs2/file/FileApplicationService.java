package io.mopar.rs2.file;

import io.mopar.core.msg.Message;
import io.mopar.file.res.CreateSessionResponse;
import io.mopar.file.res.StreamFileResponse;
import io.mopar.rs2.Application;
import io.mopar.rs2.ApplicationService;
import io.mopar.rs2.msg.file.FileRequestMessage;
import io.mopar.rs2.msg.StatusMessage;
import io.mopar.rs2.msg.handshake.FileServiceHandshakeMessage;
import io.mopar.file.FileService;
import io.mopar.rs2.net.Session;
import io.mopar.rs2.net.SessionClosedMessage;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.net.packet.PacketMetaList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hadyn Fitzgerald
 */
public class FileApplicationService extends ApplicationService<FileService> {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(FileApplicationService.class);

    /**
     * The service handshake.
     */
    private static final PacketMetaData HANDSHAKE_PACKET = new PacketMetaData(15, "file_handshake", 4);

    /**
     * Constructs a new {@link FileApplicationService};
     *
     * @param service The service.
     */
    public FileApplicationService(FileService service) {
        super(service);
    }

    /**
     * Initializes the delegator.
     *
     * @param app The application.
     */
    @Override
    public void setup(Application app) {
        // Register the codec initializer
        app.register(new FileMessageCodecInitializer());

        // Register all of the incoming packets
        registerIncomingPackets(app.getIncomingPackets());

        // Register all of the message handlers
        registerMessageHandler(FileRequestMessage.class, this::handleFileRequest);
        registerMessageHandler(SessionClosedMessage.class, this::handleSessionClosed);

        // Register the update service handshake
        app.registerMessageHandler(FileServiceHandshakeMessage.class, this::handleUpdateHandshakeMessage);
    }

    /**
     * Registers all of the incoming packets for the service.
     *
     * @param incomingPackets The incoming packets.
     */
    private void registerIncomingPackets(PacketMetaList incomingPackets) {
        incomingPackets.add(HANDSHAKE_PACKET);
    }

    /**
     * Handles a request to be use the update service.
     *
     * @param session The session that the message was received from.
     * @param message The message.
     */
    private void handleUpdateHandshakeMessage(Session session, FileServiceHandshakeMessage message) {
        service.createSession(res -> handleCreateSessionResponse(session, res));
    }

    /**
     * Handles a create file session response.
     *
     * @param session The session.
     * @param res The response.
     */
    private void handleCreateSessionResponse(Session session, CreateSessionResponse res) {
        switch (res.getStatus()) {

            /**
             * Session was successfully created.
             */
            case CreateSessionResponse.OK:
                session.attach(FileSessionContext.class, new FileSessionContext(res.getSession()));
                session.setDispatcher(dispatcher);
                session.pipeline().replace("packet_decoder", "file_request_decoder", new FileServiceRequestDecoder());
                session.pipeline().addBefore("channel_handler", "chunk_encoder", new FileChunkEncoder());
                session.writeAndFlush(StatusMessage.OK);
                break;

            /**
             * File service has too many active sessions.
             */
            case CreateSessionResponse.FULL:
                session.writeAndFlush(StatusMessage.FULL);
                break;
        }
    }

    /**
     * Handles a file request.
     *
     * @param session The session the message was received from.
     * @param request The file request message.
     */
    private void handleFileRequest(Session session, FileRequestMessage request) {
        FileSessionContext ctx = session.get(FileSessionContext.class);

        service.stream(ctx.getSession().getId(), request.getVolumeId(), request.getFileId(), request.isPriority(),
                new SessionFileChunkStream(session), (res) -> handleStreamFileResponse(session, res));
    }

    /**
     * Handles a stream file response.
     *
     * @param session The session.
     * @param res The response.
     */
    private void handleStreamFileResponse(Session session, StreamFileResponse res) {}

    /**
     * Handles when a session is closed. Removes the associated file session from the file service.
     *
     * @param session The session that was closed.
     * @param message The message.
     */
    private void handleSessionClosed(Session session, SessionClosedMessage message) {
        logger.info("File service session closed");
        FileSessionContext ctx = session.get(FileSessionContext.class);
        service.removeSession(ctx.getSession().getId(), (res) -> {});
    }
}
