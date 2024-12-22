package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.runnable.ExceptingRunnable;
import com.github.cao.awa.sinuatum.function.ecception.supplier.ExceptingSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SupplyManipulate<R> extends Manipulate<SupplyManipulate<R>> {
    private final ExceptingSupplier<R, Throwable> supplier;

    public SupplyManipulate(ExceptingSupplier<R, Throwable> supplier) {
        this.supplier = supplier;
    }

    @Override
    public SupplyManipulate<R> beSelf() {
        return this;
    }

    public R get() {
        try {
            return this.supplier.get();
        } catch (Throwable throwable) {
            handleThrowable(throwable);
            return null;
        }
    }

    public R getOr(@NotNull R defaultValue) {
        R result = get();
        return result == null ? defaultValue : result;
    }

    public R getOr(@NotNull Supplier<R> supplier) {
        R result = get();
        return result == null ? supplier.get() : result;
    }
}
