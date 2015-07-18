package io.mopar.world;

import io.mopar.core.Callback;
import io.mopar.core.Service;
import io.mopar.world.req.SnapshotRequest;
import io.mopar.world.res.SnapshotResponse;

/**
 * @author Hadyn Fitzgerald
 */
public class WorldService extends Service {

    /**
     * The maximum amount of locations allowed to be registered.
     */
    public static final int LOCATION_LIMIT = 255;

    /**
     * The maximum amount of worlds allowed to be registered.
     */
    public static final int WORLD_LIMIT = 500;

    /**
     * The locations.
     */
    private LocationList locations = new LocationList(LOCATION_LIMIT);

    /**
     * The worlds.
     */
    private WorldList worlds = new WorldList(WORLD_LIMIT);

    /**
     * The revision.
     */
    private int revision = 1;

    @Override
    public void setup() throws Exception {
        setBlockForRequests(true);
        Location location = new Location("US1", "US East", Location.FLAG_USA);
        worlds.set(1, new World("-", "game.moparscape.com", location));
        locations.add(location);

        registerRequestHandler(SnapshotRequest.class, this::handleSnapshotRequest);
    }

    @Override
    public void pulse() throws Exception {}

    @Override
    public void teardown() {}

    /**
     * Requests a snapshot.
     *
     * @param revision The revision to compare the current revision to.
     * @param callback The response callback.
     */
    public void requestSnapshot(int revision, Callback<SnapshotResponse> callback) {
        submit(new SnapshotRequest(revision), callback);
    }

    /**
     * Handles a snapshot request.
     *
     * @param request The request.
     * @param callback The response callback.
     */
    private void handleSnapshotRequest(SnapshotRequest request, Callback callback) {
        SnapshotResponse response = new SnapshotResponse(SnapshotResponse.OK);
        response.setSnapshot(new Snapshot(locations, worlds, revision));

        response.setFlag(SnapshotResponse.POPULATION_UPDATED);

        if(request.getRevision() != revision) {
            response.setFlag(SnapshotResponse.WORLDS_UPDATED);
        }

        callback.call(response);
    }
}
