package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class BlockTile {

    /**
     * The x coordinate, relative to the chunk the tile is within.
     */
    private int x;

    /**
     * The y coordinate, relative to the chunk the tile is within.
     */
    private int y;

    /**
     * The wall located at the tile.
     */
    private GameObject wall;

    /**
     * The roof located at the tile.
     */
    private GameObject roof;

    /**
     * TODO
     */
    private GameObject gameObject2;

    /**
     * TODO
     */
    private GameObject gameObject3;


    /**
     * Constructs a new {@link BlockTile};
     *
     * @param x the coordinate x.
     * @param y the coordinate y.
     */
    public BlockTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param group
     * @param gameObject
     */
    public void setLocale(int group, GameObject gameObject) {
        switch (group) {
            case GameObjectGroup.WALL:
                wall = gameObject;
                break;
            case GameObjectGroup.ROOF:
                roof = gameObject;
                break;
            case GameObjectGroup.GROUP_2:
                gameObject2 = gameObject;
                break;
            case GameObjectGroup.GROUP_3:
                gameObject3 = gameObject;
                break;
            default:
                throw new IllegalArgumentException("Unhandled group");
        }
    }

    /**
     *
     * @param group
     * @return
     */
    public GameObject getGameObject(int group) {
        switch (group) {
            case GameObjectGroup.WALL:
                return wall;
            case GameObjectGroup.ROOF:
                return roof;
            case GameObjectGroup.GROUP_2:
                return gameObject2;
            case GameObjectGroup.GROUP_3:
                return gameObject3;
            default:
                throw new IllegalArgumentException("Unhandled group");
        }
    }

    public void removeGameObject(int group) {
        switch (group) {
            case GameObjectGroup.WALL:
                wall = null;
                break;
            case GameObjectGroup.ROOF:
                roof = null;
                break;
            case GameObjectGroup.GROUP_2:
                gameObject2 = null;
                break;
            case GameObjectGroup.GROUP_3:
                gameObject3 = null;
                break;
            default:
                throw new IllegalArgumentException("Unhandled group");
        }
    }
}
