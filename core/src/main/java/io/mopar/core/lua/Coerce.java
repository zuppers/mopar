package io.mopar.core.lua;

import com.google.common.base.CaseFormat;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Hadyn Fitzgerald
 */
public class Coerce {

    /**
     * The composite factories.
     */
    private static Map<Class<?>, AdapterFactory<?, ? extends UserdataAdapter>> factories = new HashMap<>();

    /**
     *
     */
    private static LuaTable USERDATA_META_TABLE = new LuaTable();

    /**
     * Registers a componsite factory.
     *
     * @param objectClass The object class.
     * @param factory The composite factory.
     * @param <T> The generic object type.
     */
    public static <T> void register(Class<T> objectClass, AdapterFactory<T, ?> factory) {
        factories.put(objectClass, factory);
    }

    /**
     * Coerces a java object to a lua value either using a {@link AdapterFactory} and or {@link CoerceJavaToLua#coerce(Object)}.
     *
     * @param value The object to coerce to a lua value.
     * @return The coerced lua value.
     */
    @SuppressWarnings("unchecked")
    public static LuaValue coerceToLua(Object value) {
        if(!Objects.isNull(value)) {
            if (value.getClass().isArray()) {
                return coerceArray((Object[]) value);
            }

            AdapterFactory factory = factories.get(value.getClass());

            LuaValue luaValue;
            if (factory != null) {
                luaValue = CoerceJavaToLua.coerce(factory.create(value));
            } else {
                luaValue = CoerceJavaToLua.coerce(value);
            }

            if(luaValue.isuserdata()) {
                luaValue.setmetatable(USERDATA_META_TABLE);
            }

            return luaValue;
        } else {
            return LuaValue.NIL;
        }
    }

    /**
     *
     * @param arr
     * @return
     */
    private static LuaTable coerceArray(Object[] arr) {
        LuaTable table = new LuaTable();
        int i = 1;
        for(Object o : arr) {
            table.rawset(i++, coerceToLua(o));
        }
        return table;
    }

    static {
        USERDATA_META_TABLE.set("__index", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue table, LuaValue key) {
                return table.get(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, key.tojstring()));
            }
        });
    }
}