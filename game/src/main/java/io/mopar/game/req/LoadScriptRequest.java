package io.mopar.game.req;

import io.mopar.core.Request;

/**
 * Created by hadyn on 6/23/15.
 */
public class LoadScriptRequest extends Request {

    private String namespace;
    private String script;

    public LoadScriptRequest(String script) {
        this.script = script;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getScript() {
        return script;
    }
}
