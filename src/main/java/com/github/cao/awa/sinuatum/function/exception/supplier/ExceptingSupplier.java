package com.github.cao.awa.sinuatum.function.exception.supplier;

@FunctionalInterface
public interface ExceptingSupplier<T, EX extends Throwable> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws EX;
}
