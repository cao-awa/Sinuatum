package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.runnable.ExceptingRunnable;

public class RunManipulate extends Manipulate<RunManipulate> {
    private final ExceptingRunnable<Throwable> runnable;

    public RunManipulate(ExceptingRunnable<Throwable> runnable) {
        this.runnable = runnable;
    }

    @Override
    public RunManipulate beSelf() {
        return this;
    }

    public void run() {
        try {
            this.runnable.run();
        } catch (Throwable throwable) {
            handleThrowable(throwable);
        }
    }
}
