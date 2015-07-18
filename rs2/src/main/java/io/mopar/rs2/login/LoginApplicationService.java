package io.mopar.rs2.login;

import io.mopar.login.LoginService;
import io.mopar.rs2.Application;
import io.mopar.rs2.ApplicationService;
import io.mopar.rs2.game.GameApplicationService;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.net.packet.PacketMetaList;

/**
 * @author Hadyn Fitzgerald
 */
public class LoginApplicationService extends ApplicationService<LoginService> {

    /**
     * The game application service.
     */
    private GameApplicationService gameService;

    /**
     * Constructs a new {@link LoginApplicationService};
     *
     * @param service The service.
     */
    public LoginApplicationService(LoginService service) {
        super(service);
    }

    @Override
    public void setup(Application app) {
        // Register the codec initializer
        app.register(new LoginMessageCodecInitializer());

        // Register the incoming packets
        registerIncomingPackets(app.getIncomingPackets());
    }


    /**
     * Registers the incoming packets.
     *
     * @param incomingPackets The incoming packet list.
     */
    private void registerIncomingPackets(PacketMetaList incomingPackets) {
        incomingPackets.add(new PacketMetaData(14, "login_status", 1));
        incomingPackets.add(new PacketMetaData(16, "login_request_new", PacketMetaData.VAR_SHORT_LENGTH));
        incomingPackets.add(new PacketMetaData(18, "login_request_reconnect", PacketMetaData.VAR_SHORT_LENGTH));
    }
}
