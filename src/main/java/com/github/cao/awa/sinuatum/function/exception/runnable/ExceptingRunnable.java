package com.github.cao.awa.sinuatum.function.exception.runnable;

@FunctionalInterface
public interface ExceptingRunnable<EX extends Throwable> {
    /**
     * Runs this operation.
     */
    void run() throws EX;
}
