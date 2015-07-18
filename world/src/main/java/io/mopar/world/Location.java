package io.mopar.world;

/**
 * Created by hadyn on 7/5/2015.
 */
public class Location {

    public static final int FLAG_AUSTRALIA   = 16;
    public static final int FLAG_BELGIUM     = 22;
    public static final int FLAG_BRAZIL      = 31;
    public static final int FLAG_CANADA      = 38;
    public static final int FLAG_DENMARK     = 58;
    public static final int FLAG_FINLAND     = 69;
    public static final int FLAG_UK          = 77;
    public static final int FLAG_IRELAND     = 101;
    public static final int FLAG_MEXICO      = 152;
    public static final int FLAG_NETHERLANDS = 161;
    public static final int FLAG_NORWAY      = 162;
    public static final int FLAG_SWEDEN      = 191;
    public static final int FLAG_USA         = 225;

    /**
     * The id.
     */
    private int id;

    /**
     * The location tag.
     */
    private String tag;

    /**
     * The name.
     */
    private String name;

    /**
     * The flag.
     */
    private int flag;

    /**
     * Constructs a new {@link Location};
     *
     * @param tag The tag.
     * @param name The name.
     * @param flag The flag.
     */
    public Location(String tag, String name, int flag) {
        this.tag = tag;
        this.name = name;
        this.flag = flag;
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

    public String getName() {
        return name;
    }

    public int getFlag() {
        return flag;
    }
}