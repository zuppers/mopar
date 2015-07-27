package io.mopar.rs2.account;

import com.google.gson.JsonParser;
import io.mopar.account.AccountService;
import io.mopar.account.res.LoginResponse;
import io.mopar.account.res.RegistrationResponse;
import io.mopar.account.res.UsernameQueryResponse;
import io.mopar.core.Callback;
import io.mopar.core.asset.AssetLoaderException;
import io.mopar.rs2.Application;
import io.mopar.rs2.ApplicationService;
import io.mopar.rs2.msg.StatusMessage;
import io.mopar.rs2.net.Session;
import io.mopar.rs2.net.packet.PacketMetaData;
import io.mopar.rs2.net.packet.PacketMetaList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * @author Hadyn Fitzgerald
 */
public class AccountApplicationService extends ApplicationService<AccountService> {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountApplicationService.class);

    /**
     * Constructs a new {@link AccountApplicationService};
     *
     * @param service The service.
     */
    public AccountApplicationService(AccountService service) {
        super(service);
    }

    @Override
    public void setup(Application app) {
        try {
            // Load all of the incoming packets for the service
            JsonParser parser = new JsonParser();
            app.getIncomingPackets().parse(parser.parse(new InputStreamReader(
                    new ByteArrayInputStream(app.loadAsset("account/incoming-packets.json")))));
        } catch (URISyntaxException | AssetLoaderException ex) {
            logger.error("Failed to load incoming packet data", ex);
        }

        // Register the message codec initializer and message handlers
        app.register(new AccountMessageCodecInitializer());
        registerMessageHandlers(app);

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

    /**
     *
     * @param username
     * @param password
     * @param callback
     */
    public void login(long username, String password, Callback<LoginResponse> callback) {
        service.login(username, password, callback);
    }

    /**
     *
     * @param app
     */
    private void registerMessageHandlers(Application app) {
        app.registerMessageHandler(ValidatePersonalDetailsMessage.class, this::handleValidatePersonalDetailsMessage);
        app.registerMessageHandler(QueryUsernameMessage.class, this::handleQueryUsernameMessage);
        app.registerMessageHandler(RegistrationRequestMessage.class, this::handleRegistrationRequestMessage);
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleValidatePersonalDetailsMessage(Session session, ValidatePersonalDetailsMessage message) {
        session.writeAndFlush(StatusMessage.CONTINUE);
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleQueryUsernameMessage(Session session, QueryUsernameMessage message) {
        service.queryUsername(message.getUsername(), (res) -> handleUsernameQueryResponse(session, res));
    }

    /**
     *
     * @param session
     * @param response
     */
    private void handleUsernameQueryResponse(Session session, UsernameQueryResponse response) {
        switch (response.getStatus()) {
            case UsernameQueryResponse.ACCEPTED:
                break;

            case UsernameQueryResponse.TAKEN:
                UsernameSuggestionMessage message = new UsernameSuggestionMessage();
                session.writeAndFlush(message);
                break;

            case UsernameQueryResponse.UNIQUE:
                session.writeAndFlush(StatusMessage.CONTINUE);
                break;
        }
    }

    /**
     *
     * @param session
     * @param message
     */
    private void handleRegistrationRequestMessage(Session session, RegistrationRequestMessage message) {
        service.register(message.getUsername(), message.getPassword(), (res) -> handleRegistrationResponse(session, res));
    }

    /**
     *
     * @param session
     * @param res
     */
    private void handleRegistrationResponse(Session session, RegistrationResponse res) {
        switch (res.getStatus()) {
            case RegistrationResponse.ACCEPTED:
                return;

            case RegistrationResponse.USER_ALREADY_EXISTS:
                UsernameSuggestionMessage message = new UsernameSuggestionMessage();
                session.writeAndFlush(message);
                break;

            case RegistrationResponse.OK:
                session.writeAndFlush(StatusMessage.CONTINUE);
                break;
        }
    }
}
