package io.mopar.game.lua.model;

import io.mopar.core.lua.UserdataAdapter;
import io.mopar.game.config.VarbitConfig;
import io.mopar.game.config.VariableConfig;
import io.mopar.game.model.Entity;
import io.mopar.game.model.Position;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class EntityAdapter implements UserdataAdapter {

    /**
     * The entity.
     */
    private Entity entity;

    /**
     * Constructs a new {@link EntityAdapter};
     *
     * @param entity the entity.
     */
    protected EntityAdapter(Entity entity) {
        this.entity = entity;
    }

    /**
     *
     * @return
     */
    public int GetX() {
        Position position = entity.getPosition();
        return position.getX();
    }

    /**
     *
     * @return
     */
    public int GetY() {
        Position position = entity.getPosition();
        return position.getY();
    }

    /**
     *
     * @return
     */
    public int GetPlane() {
        Position position = entity.getPosition();
        return position.getPlane();
    }

    /**
     * Sets the value of a variable as a boolean.
     *
     * @param config the configuration.
     * @param bool the boolean value.
     */
    public void SetBool(VariableConfig config, boolean bool) {
        Set(config, bool ? 1 : 0);
    }

    /**
     * Sets the value of a variable.
     *
     * @param config the configuration.
     * @param value the value.
     */
    public void Set(VariableConfig config, int value) {
        Set(config.getId(), value);
    }

    /**
     * Sets the value of a variable.
     *
     * @param id the id.
     * @param value the value.
     */
    public void Set(int id, int value) {
        entity.setVariable(id, value);
    }

    /**
     * Gets the value of a variable.
     *
     * @param config the configuration.
     * @return the value.
     */
    public int Get(VariableConfig config) {
        return Get(config.getId());
    }

    /**
     * Gets the value of a variable.
     *
     * @param id the id.
     * @return the value.
     */
    public int Get(int id) {
        return entity.getVariable(id);
    }


    /**
     * Gets the value of a variable as a boolean.
     *
     * @param config the configuration.
     * @return the boolean value.
     */
    public boolean GetBool(VariableConfig config) {
        return Get(config) == 1;
    }

    /**
     *
     * @param config
     */
    public void Toggle(VariableConfig config) {
        SetBool(config, !GetBool(config));
    }

    /**
     *
     * @param config
     * @param value
     */
    public void SetBit(VarbitConfig config, int value) {
        SetBit(config.getVariableId(), config.getOffset(), config.getLength(), value);
    }

    /**
     *
     * @param variableId
     * @param off
     * @param len
     * @param value
     */
    public void SetBit(int variableId, int off, int len, int value) {
        entity.setBitVariable(variableId, off, len, value);
    }

    /**
     *
     * @param config
     * @return
     */
    public int GetBit(VarbitConfig config) {
        return GetBit(config.getVariableId(), config.getOffset(), config.getLength());
    }

    /**
     *
     * @param variableId
     * @param off
     * @param len
     * @return
     */
    public int GetBit(int variableId, int off, int len) {
        return entity.getBitVariable(variableId, off, len);
    }

    /**
     *
     * @param config
     * @param bool
     */
    public void SetBool(VarbitConfig config, boolean bool) {
        SetBit(config, bool ? 1 : 0);
    }

    /**
     *
     * @param config
     * @return
     */
    public boolean GetBool(VarbitConfig config) {
        return GetBit(config) == 1;
    }

}
