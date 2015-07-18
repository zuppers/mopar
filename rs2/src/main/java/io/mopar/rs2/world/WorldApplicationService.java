package io.mopar.rs2.world;

import io.mopar.rs2.Application;
import io.mopar.rs2.ApplicationService;
import io.mopar.rs2.msg.handshake.WorldServiceHandshake;
import io.mopar.rs2.msg.world.SnapshotMessage;
import io.mopar.rs2.net.Session;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.world.WorldService;
import io.mopar.world.res.SnapshotResponse;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldApplicationService extends ApplicationService<WorldService> {

    /**
     * The service handshake.
     */
    private static final PacketMetaData HANDSHAKE_PACKET = new PacketMetaData(255, "worldlist_handshake", 4);

    /**
     * Constructs a new {@link WorldApplicationService};
     *
     * @param service The service.
     */
    public WorldApplicationService(WorldService service) {
        super(service);
    }

    @Override
    public void setup(Application app) {
        // Register all the incoming packets
        registerIncomingPackets(app);

        // Bind the codec initializer
        app.register(new WorldMessageCodecInitializer());

        // Register all the message handlers
        app.registerMessageHandler(WorldServiceHandshake.class, this::handleHandshake);
    }

    /**
     * Registers all of the incoming packets for the service.
     *
     * @param app The application to register the packets to.
     */
    private void registerIncomingPackets(Application app) {
        app.getIncomingPackets().add(HANDSHAKE_PACKET);
    }

    /**
     * Handles a service handshake message.
     *
     * @param session The session.
     * @param handshake The message.
     */
    private void handleHandshake(Session session, WorldServiceHandshake handshake) {
        service.requestSnapshot(handshake.getRevision(), res -> handleSnapshotResponse(session, res));
    }

    /**
     * Handles a snapshot response.
     *
     * @param session The session.
     * @param response The response.
     */
    private void handleSnapshotResponse(Session session, SnapshotResponse response) {
        SnapshotMessage message = new SnapshotMessage(response.getSnapshot());
        message.setWorldsUpdated(response.isActive(SnapshotResponse.WORLDS_UPDATED));
        message.setPopulationUpdated(response.isActive(SnapshotResponse.POPULATION_UPDATED));
        session.writeAndFlush(message);
    }
}
