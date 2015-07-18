package io.mopar.world;

/**
 * @author Hadyn Fitzgerald
 */
public class Snapshot {

    /**
     * The locations.
     */
    private LocationList locations;

    /**
     * The worlds.
     */
    private WorldList worlds;

    /**
     * The revision.
     */
    private int revision;

    /**
     * Constructs a new {@link Snapshot};
     *
     * @param locations The locations.
     * @param worlds The worlds.
     * @param revision The revision.
     */
    public Snapshot(LocationList locations, WorldList worlds, int revision) {
        this.locations = locations;
        this.worlds = worlds;
        this.revision = revision;
    }

    /**
     * Gets the locations.
     *
     * @return The locations.
     */
    public LocationList getLocations() {
        return locations;
    }

    /**
     * Gets the worlds.
     *
     * @return The worlds.
     */
    public WorldList getWorlds() {
        return worlds;
    }

    /**
     * Gets the revision.
     *
     * @return The revision.
     */
    public int getRevision() {
        return revision;
    }
}