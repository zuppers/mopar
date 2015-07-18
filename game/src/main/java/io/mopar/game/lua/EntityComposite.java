package io.mopar.game.lua;

import io.mopar.core.lua.Composite;
import io.mopar.game.model.Entity;

/**
 * @author Hadyn Fitzgerald
 */
public class EntityComposite implements Composite {

    private Entity entity;

    public EntityComposite(Entity entity) {
        this.entity = entity;
    }
}
