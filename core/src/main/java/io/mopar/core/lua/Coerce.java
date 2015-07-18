package io.mopar.core.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class Coerce {

    /**
     * The composite factories.
     */
    private static Map<Class<?>, CompositeFactory<?, ? extends Composite>> factories = new HashMap<>();

    /**
     * Registers a componsite factory.
     *
     * @param objectClass The object class.
     * @param factory The composite factory.
     * @param <T> The generic object type.
     */
    public static <T> void register(Class<T> objectClass, CompositeFactory<T, ?> factory) {
        factories.put(objectClass, factory);
    }

    /**
     * Coerces a java object to a lua value either using a {@link CompositeFactory} and or {@link CoerceJavaToLua#coerce(Object)}.
     *
     * @param value The object to coerce to a lua value.
     * @return The coerced lua value.
     */
    @SuppressWarnings("unchecked")
    public static LuaValue coerceToLua(Object value) {
        CompositeFactory factory = factories.get(value.getClass());
        if(factory != null) {
            return CoerceJavaToLua.coerce(factory.create(value));
        }
        return CoerceJavaToLua.coerce(value);
    }
}