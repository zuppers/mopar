package io.mopar.core;

/**
 * @author Hadyn Fitzgerald
 *
 * Callback for responses.
 */
public interface Callback<T extends Response> {

    /**
     * Calls back a value.
     *
     * @param value The value.
     */
    void call(T value);
}
