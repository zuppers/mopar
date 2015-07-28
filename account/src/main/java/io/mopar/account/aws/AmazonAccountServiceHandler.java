package io.mopar.account.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.PersistableTransfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.internal.S3ProgressListener;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.model.*;
import io.mopar.account.AccountServiceHandler;
import io.mopar.account.DefaultProfileCodec;
import io.mopar.account.Profile;
import io.mopar.account.ProfileCodec;
import io.mopar.account.req.*;
import io.mopar.account.res.*;
import io.mopar.core.Base37;
import io.mopar.core.Callback;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.mopar.util.ListUtil.first;

/**
 * @author Hadyn Fitzgerald
 */
public class AmazonAccountServiceHandler implements AccountServiceHandler {

    private static final Logger logger = LoggerFactory.getLogger(AmazonAccountServiceHandler.class);

    private static final String USER_KEY    = "user";
    private static final String PROFILE_EXT = ".mopr";

    private AmazonSimpleDBAsync client;
    private TransferManager transferManager;
    private ProfileCodec codec = new DefaultProfileCodec();
    private Map<Long, Profile> profiles = new ConcurrentHashMap<>();
    private String environment;

    public AmazonAccountServiceHandler(AmazonSimpleDBAsync client, TransferManager transferManager) {
        this(client, transferManager, "production");
    }

    public AmazonAccountServiceHandler(AmazonSimpleDBAsync client, TransferManager transferManager, String environment) {
        this.client = client;
        this.transferManager = transferManager;
        this.environment = environment;
    }

    @Override
    public void loadProfile(LoadProfileRequest request, Callback<LoadProfileResponse> callback) {

        // If the profile takes a hit because it is not full saved yet then just load the given profile
        if(profiles.containsKey(request.getUid())) {
            LoadProfileResponse response = new LoadProfileResponse(LoadProfileResponse.OK);
            response.setProfile(profiles.get(request.getUid()));
            callback.call(response);
            return;
        }

        GetObjectRequest req = new GetObjectRequest(getBucketIdentifier(request.getUid()), getProfileFileKey(request.getUid()));
        try {
            Path path = Files.createTempFile(Long.toString(request.getUid()), PROFILE_EXT);

            // Flag that file to delete on exit
            File file = path.toFile();
            file.deleteOnExit();

            transferManager.download(req, file, new S3ProgressListener() {
                @Override
                public void onPersistableTransfer(PersistableTransfer persistableTransfer) {}

                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    if(progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
                        logger.info("Finished downloading profile from server; user: " + Base37.decode(request.getUid()));

                        try {
                            // Read the file into memory and then decode the profile
                            byte[] bytes = Files.readAllBytes(path);
                            Profile profile = codec.decode(bytes);

                            LoadProfileResponse response = new LoadProfileResponse(LoadProfileResponse.OK);
                            response.setProfile(profile);
                            callback.call(response);
                        } catch (IOException ex) {
                            logger.error("Encountered I/O exception while trying to decode profile", ex);
                            callback.call(new LoadProfileResponse(LoadProfileResponse.INTERNAL_ERROR));
                        }
                    }
                }
            });

        } catch (Exception ex) {
            if(ex instanceof AmazonServiceException) {
                AmazonServiceException serviceException = (AmazonServiceException) ex;

                // Check to see if the profile simply couldn't be found
                if(serviceException.getStatusCode() == 404) {
                    logger.info("Failed to load profile, does not exist; user: " + Base37.decode(request.getUid()));
                    callback.call(new LoadProfileResponse(LoadProfileResponse.PROFILE_DOES_NOT_EXIST));
                    return;
                }
            }

            logger.error("Unhandled exception encountered while trying to download profile", ex);
            callback.call(new LoadProfileResponse(LoadProfileResponse.INTERNAL_ERROR));
        }
    }

    /**
     *
     * @param request
     * @param callback
     */
    @Override
    public void saveProfile(SaveProfileRequest request, Callback<SaveProfileResponse> callback) {
        Profile profile = request.getProfile();
        profiles.put(profile.getUid(), profile);

        try {
            Path path = Files.createTempFile(Long.toString(profile.getUid()), PROFILE_EXT);

            // Flag that file to delete on exit
            File file = path.toFile();
            file.deleteOnExit();

            // Encode the profile and write it out the temporary file
            Files.write(path, codec.encode(profile));

            PutObjectRequest req = new PutObjectRequest(getBucketIdentifier(profile.getUid()), getProfileFileKey(profile.getUid()), file);
            transferManager.upload(req, new S3ProgressListener() {
                @Override
                public void onPersistableTransfer(PersistableTransfer persistableTransfer) {}

                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    if(progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
                        logger.info("Profile upload completed, user: " + Base37.decode(request.getProfile().getUid()));
                        profiles.remove(profile.getUid());
                        callback.call(new SaveProfileResponse(SaveProfileResponse.OK));
                    }
                }
            });
        } catch (Exception ex) {
            logger.error("Internal exception encountered while trying to save profile", ex);
            callback.call(new SaveProfileResponse(SaveProfileResponse.INTERNAL_ERROR));
        }
    }

    @Override
    public void login(LoginRequest request, Callback<LoginResponse> callback) {
        SelectRequest req = new SelectRequest();
        req.setSelectExpression("select password from `" + getShardIdentifier(environment, request.getUsername())
                + "` where itemName() = '" + request.getUsername() + ".user' limit 1");

        client.selectAsync(req, new AsyncHandler<SelectRequest, SelectResult>() {

            @Override
            public void onSuccess(SelectRequest r, SelectResult result) {

                // Check and see if there are any users registered to the selected name, if not
                // then just respond a generic invalid user or password status code.
                List<Item> items = result.getItems();
                if (items.isEmpty()) {
                    logger.info("Failed to authenticate user, invalid user; username: " + Base37.decode(request.getUsername()));
                    callback.call(new LoginResponse(LoginResponse.INVALID_USER_OR_PASS));
                    return;
                }

                Item item = first(result.getItems());

                // Extract the query information
                String pass = SimpleDBUtil.getAttributeValue(item, 0);

                // Check and see if the password matches the hashed entry in the database.
                if (!BCrypt.checkpw(request.getPassword(), pass)) {
                    logger.info("Failed to authenticate user, invalid password; username: " + Base37.decode(request.getUsername()));
                    callback.call(new LoginResponse(LoginResponse.INVALID_USER_OR_PASS));
                    return;
                }

                // Call back that everything went okay :)
                logger.info("Authenticated user; id: username: " + Base37.decode(request.getUsername()));

                // After we authenticate the user load the player profile if it exists
                loadProfile(request.getUsername(), (res) -> {
                    LoginResponse response = new LoginResponse(LoginResponse.OK);
                    switch (response.getStatus()) {
                        case LoadProfileResponse.OK:
                            response.setProfile(res.getProfile());
                            break;

                        // If we encounter an internal error report it back to the client and don't continue forward
                        case LoadProfileResponse.INTERNAL_ERROR:
                            callback.call(new LoginResponse(LoginResponse.INTERNAL_ERROR));
                            return;
                    }
                    callback.call(response);
                });
            }

            @Override
            public void onError(Exception ex) {
                logger.error("Internal exception encountered while trying to authenticate user", ex);
                callback.call(new LoginResponse(LoginResponse.INTERNAL_ERROR));
            }
        });
    }

    @Override
    public void register(RegistrationRequest request, Callback<RegistrationResponse> callback) {
        PutAttributesRequest req = new PutAttributesRequest();

        req.setDomainName(getShardIdentifier(environment, request.getUsername()));
        req.setItemName(getAccountItemIdentifier(request.getUsername(), USER_KEY));

        // Only execute this request if the user item does not exist
        UpdateCondition condition = new UpdateCondition();
        condition.setName("password");
        req.setExpected(condition.withExists(false));

        Collection<ReplaceableAttribute> attributes = new ArrayList<>();

        // Hash the password and store it into an attribute
        ReplaceableAttribute passwordAttribute = new ReplaceableAttribute();
        passwordAttribute.setName("password");
        passwordAttribute.setValue(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));

        attributes.add(passwordAttribute);

        // Set the request attributes
        req.setAttributes(attributes);

        client.putAttributesAsync(req, new AsyncHandler<PutAttributesRequest, Void>() {

            @Override
            public void onSuccess(PutAttributesRequest r, Void $_) {
                logger.info("Successfully registered account; user: " + Base37.decode(request.getUsername()));
                callback.call(new RegistrationResponse(RegistrationResponse.OK));
            }

            @Override
            public void onError(Exception exception) {
                if(exception instanceof AmazonServiceException) {
                    AmazonServiceException serviceException = (AmazonServiceException) exception;

                    // Multi valued attribute conditional check failed
                    // Sent when a user with the specified username already exists
                    if(serviceException.getStatusCode() == 409) {
                        logger.info("Failed to register user, specified user already exists");
                        callback.call(new RegistrationResponse(RegistrationResponse.USER_ALREADY_EXISTS));
                        return;
                    }
                }

                logger.error("Internal exception encountered while trying to register user", exception);
                callback.call(new RegistrationResponse(RegistrationResponse.INTERNAL_ERROR));
            }
        });
    }

    @Override
    public void queryUsername(UsernameQueryRequest request, Callback<UsernameQueryResponse> callback) {
        String expression = "select itemName() from `" + getShardIdentifier(environment, request.getUsername()) + "` where itemName() = '" +
                getAccountItemIdentifier(request.getUsername(), USER_KEY) + "' limit 1";

        SelectRequest req = new SelectRequest();
        req.setSelectExpression(expression);

        // Submit the request to the client
        client.selectAsync(req, new AsyncHandler<SelectRequest, SelectResult>() {

            @Override
            public void onSuccess(SelectRequest r, SelectResult result) {
                logger.info("Successfully queried username; user: " + Base37.decode(request.getUsername()));
                List<Item> items = result.getItems();
                if(!items.isEmpty()) {
                    callback.call(new UsernameQueryResponse(UsernameQueryResponse.TAKEN));
                    return;
                }
                callback.call(new UsernameQueryResponse(UsernameQueryResponse.UNIQUE));
            }

            @Override
            public void onError(Exception exception) {
                logger.error("Internal exception encountered while trying to query username; user: "
                        + Base37.decode(request.getUsername()), exception);
                callback.call(new UsernameQueryResponse(UsernameQueryResponse.INTERNAL_ERROR));
            }
        });
    }

    /**
     * Gets the identifier for a specific shard, or domain, that the player information is stored in.
     *
     * @param username the username.
     * @return the identifier.
     */
    public static String getShardIdentifier(String environment, long username) {
        int shardId = (int) (username >> 16L & 0x1f);
        return environment + ".players." + shardId;
    }

    /**
     * Gets an account item identifier.
     *
     * @param username the username.
     * @param key the item key.
     * @return the identifier.
     */
    public static String getAccountItemIdentifier(long username, String key) {
        return username + "." + key;
    }

    /**
     *
     * @param username
     * @return
     */
    public static String getBucketIdentifier(long username) {
        int shardId = (int) (username >> 16L & 0x1f);
        return shardId + ".game.moparscape.com";
    }

    private static String getProfileFileKey(long username) {
        return username + PROFILE_EXT;
    }
}
