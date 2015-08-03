package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class GameObject extends Immobile {

    public static final int REMOVED = -1;

    private int type;
    private int config;
    private int orientation;
    private boolean natural;

    private int normalType;
    private int normalConfig;
    private int normalOrientation;

    /**
     *
     * @param config
     * @param type
     * @param orientation
     */
    public GameObject(int config, int type, int orientation) {
        this(config, type, orientation, false);
    }

    /**
     * Constructs a new {@link GameObject};
     *
     * @param config the configuration.
     * @param type the type.
     * @param orientation the orientation.
     * @param natural if the object is natural.
     */
    public GameObject(int config, int type, int orientation, boolean natural) {
        this.type = type;
        this.config = config;
        this.orientation = orientation;
        this.natural = natural;

        if(natural) {
            normalType = type;
            normalConfig = config;
            normalOrientation = orientation;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public int getConfigId() {
        return config;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     *
     * @return
     */
    public boolean isNormal() {
        return natural ? type == normalType && config == normalConfig && orientation == normalOrientation : type == -1;
    }

    /**
     *
     * @return
     */
    public boolean isNatural() {
        return natural;
    }

    public int getType() {
        return type;
    }

    public int getOrientation() {
        return orientation;
    }



    public boolean isRemoved() {
        return config == -1;
    }
}
