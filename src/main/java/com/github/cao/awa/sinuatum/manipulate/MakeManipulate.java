package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.exception.consumer.ExceptingConsumer;

public class MakeManipulate<I> extends Manipulate<MakeManipulate<I>> {
    private final ExceptingConsumer<I, Throwable> consumer;

    public MakeManipulate(ExceptingConsumer<I, Throwable> consumer) {
        this.consumer = consumer;
    }

    @Override
    public MakeManipulate<I> beSelf() {
        return this;
    }

    public I make(I input) {
        try {
            this.consumer.accept(input);
        } catch (Throwable throwable) {
            handleThrowable(throwable);
        }
        return input;
    }
}
