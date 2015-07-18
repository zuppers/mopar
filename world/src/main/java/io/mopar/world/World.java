package io.mopar.world;

/**
 * @author Hadyn Fitzgerald
 */
public class World {

    /**
     * The world id.
     */
    private int id;

    /**
     * The world activity.
     */
    private String activity = "-";

    /**
     * The address.
     */
    private String address;

    /**
     * The location.
     */
    private Location location;

    /**
     * The attributes.
     */
    private int attributes;

    /**
     * The population.
     */
    private int population;

    /**
     * Constructs a new {@link World};
     *
     * @param activity
     * @param address
     * @param location
     */
    public World(String activity, String address, Location location) {
        this.activity = activity;
        this.address = address;
        this.location = location;
    }

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
     * Gets the location.
     *
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the attributes.
     *
     * @return The attributes.
     */
    public int getAttributes() {
        return attributes;
    }

    /**
     * Gets the activity.
     *
     * @return The activity.
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Gets the host address.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the population.
     *
     * @return The population.
     */
    public int getPopulation() {
        return population;
    }
}
