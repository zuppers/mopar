package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * @author Hadyn Fitzgerald
 */
public class EvalScriptRequest extends Request {

    /**
     * The script.
     */
    private String script;

    /**
     * Constructs a new {@link EvalScriptRequest};
     *
     * @param script The script to evaluate.
     */
    public EvalScriptRequest(String script) {
        this.script = script;
    }

    /**
     * Gets the script to evaluate.
     *
     * @return The script.
     */
    public String getScript() {
        return script;
    }
}
