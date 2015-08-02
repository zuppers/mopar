package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class GameObjectGroup {

    /**
     *
     */
    private static final int[] TYPE_GROUPS = new int[] {
        0, 0, 0, 0,
        1, 1, 1, 1, 1,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        3
    };


    public static final int WALL    = 0;
    public static final int ROOF    = 1;
    public static final int GROUP_2 = 2;
    public static final int GROUP_3 = 3;

    /**
     *
     * @param type
     * @return
     */
    public static int getGroupForType(int type) {
        return TYPE_GROUPS[type];
    }

    /**
     * Prevent instantiation.
     */
    private GameObjectGroup() {}
}