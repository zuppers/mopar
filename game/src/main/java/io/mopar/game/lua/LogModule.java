package io.mopar.game.lua;

import io.mopar.core.lua.LuaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hadyn Fitzgerald
 */
public class LogModule implements LuaModule {

    /**
     * The logger.
     */
    private Logger logger;

    /**
     * Constructs a new {@link LogModule};
     *
     * @param name the logger name.
     */
    public LogModule(String name) {
       this(LoggerFactory.getLogger(name));
    }

    /**
     * Constructs a new {@linl LogLuaModule};
     *
     * @param logger the logger.
     */
    public LogModule(Logger logger) {
        this.logger = logger;
    }

    /**
     * Logs a string under the INFO level.
     *
     * @param s the message to log.
     */
    public void Info(String s) {
        logger.info(s);
    }

    /**
     * Logs a string under the debug level.
     *
     * @param s the message to log.
     */
    public void Debug(String s) {
        logger.debug(s);
    }

    /**
     * Logs a string under the ERROR level.
     *
     * @param s the message to log.
     */
    public void Error(String s) {
        logger.error(s);
    }

    @Override
    public String getNamespace() {
        return "log";
    }
}
