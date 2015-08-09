package io.mopar.core.lua;

/**
 * @author Hadyn Fitzgerald
 */
public interface AdapterFactory<T, U extends UserdataAdapter> {
    U create(T value);
}
