package io.mopar.game.model.block;

import io.mopar.game.model.BlockEvent;
import io.mopar.game.model.GameObject;

/**
 * @author Hadyn Fitzgerald
 */
public class GameObjectUpdatedEvent extends BlockEvent {

    /**
     * The game object.
     */
    private GameObject gameObject;

    /**
     *
     * @param gameObject
     */
    public GameObjectUpdatedEvent(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     *
     * @return
     */
    public GameObject getGameObject() {
        return gameObject;
    }
}
