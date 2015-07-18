package io.mopar.game.res;

import io.mopar.core.Response;

/**
 * @author Hadyn Fitzgerald
 */
public class EvalScriptResponse extends Response {
    public static final int OK = 0;
    public static final int ERROR = 1;

    private int status;
    private Object result;

    public EvalScriptResponse(int status) {
        this.status = status;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }
}
