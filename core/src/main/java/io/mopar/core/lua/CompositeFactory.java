package io.mopar.core.lua;

/**
 * @author Hadyn Fitzgerald
 */
public interface CompositeFactory<T, U extends Composite> {
    U create(T value);
}
