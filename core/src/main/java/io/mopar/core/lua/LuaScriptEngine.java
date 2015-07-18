package io.mopar.core.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JsePlatform;

import javax.script.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class LuaScriptEngine {

    /**
     * The lua mime types.
     */
    private static final String[] MIME_TYPES = { "application/lua", "application/x-lua" };

    /**
     * The compiled scripts.
     */
    private Map<String, CompiledScript> compiledScripts = new HashMap<>();

    /**
     * The script engine.
     */
    private ScriptEngine engine;

    /**
     * Constructs a new {@link LuaScriptEngine};
     */
    public LuaScriptEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();

        // Search for the engine by the known lua mime types
        for(String type : MIME_TYPES) {
            if((engine = manager.getEngineByMimeType(type)) != null) {
                break;
            }
        }

        // TODO(sinisoul): Better exception to thrown here (prefer runtime exception)?
        if(engine == null) {
            throw new UnsupportedOperationException("No lua script engines registered to the classpath");
        }
    }

    /**
     * Compiles and stores a script.
     *
     * @param namespace The script namespace.
     * @param script The script.
     * @throws ScriptException Thrown if there was an issue with compiling the provided script.
     */
    public void load(String namespace, String script) throws ScriptException {
        load(namespace, new StringReader(script));
    }

    /**
     * Compiles and stores a script.
     *
     * @param namespace The script namespace.
     * @param reader The reader.
     * @throws ScriptException Thrown if there was an issue with compiling the provided script.
     */
    public void load(String namespace, Reader reader) throws ScriptException {
        if(!(engine instanceof Compilable)) {
            throw new UnsupportedOperationException("Cannot compile scripts, unsupported by given engine");
        }
        Compilable compilable = (Compilable) engine;

        CompiledScript script = compilable.compile(reader);
        compiledScripts.put(namespace, script);
    }

    /**
     * Evaluates a script.
     *
     * @param script The script.
     * @return The result.
     * @throws ScriptException Thrown if there was an issue with evaluating the provided script.
     */
    public Object eval(String script) throws ScriptException {
        return eval(new StringReader(script));
    }

    /**
     * Evaluates a script.
     *
     * @param reader The reader.
     * @return The result.
     * @throws ScriptException Thrown if there was an issue with evaluating the provided script.
     */
    public Object eval(Reader reader) throws ScriptException {
        try {
            return engine.eval(reader);
        } catch (LuaError error) {
            throw new ScriptException(error);
        }
    }

    /**
     * Calls a pre-compiled script.
     *
     * @param namespace The script namespace.
     * @throws NullPointerException Thrown if there is no script under the specified namespace.
     * @throws ScriptException Thrown if there was an issue with evaluating the script.
     */
    public void call(String namespace) throws ScriptException {
        CompiledScript script = compiledScripts.get(namespace);
        if(script == null) {
            throw new NullPointerException("No such script registered under namespace");
        }
        script.eval(engine.getBindings(ScriptContext.ENGINE_SCOPE));
    }

    /**
     * Registers a module to the engine scope. The module will be stored at its namespace value.
     *
     * @param module The module.
     */
    public void put(LuaModule module) {
        put(module.getNamespace(), module);
    }

    /**
     * Binds a value to the engine scope.
     *
     * @param name The namespace for the value.
     * @param value The value, this is coerced to lua using {@link org.luaj.vm2.lib.jse.CoerceJavaToLua#coerce(Object)}.
     */
    public void put(String name, Object value) {
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put(name, value);
    }

    /**
     * Gets a binded value from the engine scope.
     *
     * @param name The namespace for the value.
     * @return The binded value or <code>null</code> if no value exists for the specified name. This is
     *          coerced to java using {@link org.luaj.vm2.lib.jse.CoerceLuaToJava#coerce(LuaValue, Class)}.
     */
    public Object get(String name) {
        return engine.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
    }

    /**
     * Clears all the compiled scripts.
     */
    public void clear() {
        compiledScripts.clear();
    }
}