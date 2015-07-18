package io.mopar.game.action;

/**
 * @author Hadyn Fitzgerald
 */
public enum TargetType {

    /**
     * The player target type.
     */
    PLAYER;

    /**
     * Gets if the targeted type represents an entity group.
     *
     * @param type The target type to test.
     * @return If the target type represents an entity.
     */
    public static boolean isEntityType(TargetType type) {
        return type == PLAYER;
    }
}
