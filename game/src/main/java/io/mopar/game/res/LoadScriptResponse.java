package io.mopar.game.res;

import io.mopar.core.Response;

/**
 * Created by hadyn on 6/23/15.
 */
public class LoadScriptResponse extends Response {
    public static final int OK = 0;
    public static final int ERROR = 1;

    private int status;

    public LoadScriptResponse(int status) {

    }
}
