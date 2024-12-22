package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.consumer.ExceptingConsumer;
import com.github.cao.awa.sinuatum.function.ecception.supplier.ExceptingSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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
