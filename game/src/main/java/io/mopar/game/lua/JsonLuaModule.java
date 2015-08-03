package io.mopar.game.lua;

import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import io.mopar.core.lua.Coerce;
import io.mopar.core.lua.LuaModule;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Map.Entry;

/**
 * @author Hadyn Fitzgerald
 */
public class JsonLuaModule implements LuaModule {

    /**
     * Decodes a JSON formatted string to a lua table.
     *
     * @param str The json string.
     * @return The lua table.
     */
    public LuaTable decode(String str) {
        JsonParser parser = new JsonParser();

        LuaTable table = new LuaTable();

        JsonElement element = parser.parse(str);
        appendToTable(table, element);

        return table;
    }

    private void appendToTable(LuaTable table, JsonElement element) {
        if(element.isJsonObject()) {
            for(Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                String namespace = entry.getKey();
                JsonElement innerElement = entry.getValue();
                if(innerElement.isJsonArray() || innerElement.isJsonObject()) {
                    LuaTable innerTable = new LuaTable();
                    table.set(namespace, innerTable);
                    appendToTable(innerTable, innerElement);
                } else {
                    if(innerElement.isJsonNull()) {
                        table.set(namespace, LuaValue.NIL);
                    } else if(innerElement.isJsonPrimitive()) {
                        JsonPrimitive primitive = innerElement.getAsJsonPrimitive();
                        if(primitive.isBoolean()) {
                            table.set(namespace, LuaBoolean.valueOf(primitive.getAsBoolean()));
                        } else if(primitive.isNumber()) {
                            table.set(namespace, CoerceJavaToLua.coerce(primitive.getAsDouble()));
                        } else if(primitive.isString()) {
                            table.set(namespace, primitive.getAsString());
                        }
                    }
                }
            }
        } else if(element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for(int i = 0; i < arr.size(); i++) {
                JsonElement innerElement = arr.get(i);
                if(innerElement.isJsonArray() || innerElement.isJsonObject()) {
                    LuaTable innerTable = new LuaTable();
                    table.set(i+1, innerTable);
                    appendToTable(innerTable, innerElement);
                } else {
                    if(innerElement.isJsonNull()) {
                        table.set(i+1, LuaValue.NIL);
                    } else if(innerElement.isJsonPrimitive()) {
                        JsonPrimitive primitive = innerElement.getAsJsonPrimitive();
                        if(primitive.isBoolean()) {
                            table.set(i+1, LuaBoolean.valueOf(primitive.getAsBoolean()));
                        } else if(primitive.isNumber()) {
                            table.set(i+1, Coerce.coerceToLua(primitive.getAsDouble()));
                        } else if(primitive.isString()) {
                            table.set(i+1, primitive.getAsString());
                        }
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException("Cannot append primtive or nil to table");
        }
    }

    @Override
    public String getNamespace() {
        return "json";
    }
}
