package io.mopar.core;

/**
 * Created by hadyn on 6/20/15.
 */
public interface RequestHandler<T extends Request, U extends Response> {

    void handle(T request, Callback<U> callback);
}
