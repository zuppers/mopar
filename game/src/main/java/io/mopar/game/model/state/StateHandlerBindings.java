package io.mopar.game.model.state;

import io.mopar.game.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class StateHandlerBindings {

    /**
     * The numeric representation for a player target.
     */
    public static final int PLAYER_TARGET = 0;

    /**
     * The numeric representation for a NPC target.
     */
    public static final int NPC_TARGET = 1;

    /**
     * The numeric representation of no type.
     */
    public static final int NO_TYPE = -1;

    /**
     * The handlers.
     */
    private Map<Integer, StateHandler<?>> handlers = new HashMap<>();

    /**
     * Registers a player state handler.
     *
     * @param stateId The state id.
     * @param handler The state handler.
     */
    public void registerPlayerStateHandler(int stateId, StateHandler<Player> handler) {
        registerStateHandler(PLAYER_TARGET, NO_TYPE, stateId, handler);
    }

    /**
     * Handlers a player state.
     *
     * @param player The player.
     * @param stateId The state id.
     * @return <code>true</code> if the state was successfully handled, if there was no handler for the specified
     *          state returns <code>false</code>.
     */
    public boolean handlePlayerState(Player player, int stateId) {
        StateHandler<Player> handler = (StateHandler<Player>) handlers.get(getStateHandlerKey(PLAYER_TARGET, NO_TYPE, stateId));
        if(handler == null) {
            return false;
        }
        handler.handle(player, stateId);
        return true;
    }

    /**
     * Registers a state handler.
     *
     * @param targetType The target type. {@link StateHandlerBindings#PLAYER_TARGET} and {@link StateHandlerBindings#NPC_TARGET}
     * @param typeId The type id, {@link StateHandlerBindings#NO_TYPE} may only be addressed to player targets.
     * @param stateId The state id.
     * @param handler The state handler.
     */
    public void registerStateHandler(int targetType, int typeId, int stateId, StateHandler<?> handler) {
        // TODO(sinisoul): Validate the given arguments
        handlers.put(getStateHandlerKey(targetType, typeId, stateId), handler);
    }

    /**
     * Gets a state handler hash key.
     *
     * @param targetType The target type.
     * @param typeId The type id.
     * @param stateId The state id.
     * @return The hash key for the given parameters.
     */
    private static int getStateHandlerKey(int targetType, int typeId, int stateId) {
        return (targetType & 0xf) << 28 | (typeId & 0x3fff) << 14 | (stateId & 0x3fff);
    }
}