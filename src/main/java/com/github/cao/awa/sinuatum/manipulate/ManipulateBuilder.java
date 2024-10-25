package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.consumer.ExceptingConsumer;
import com.github.cao.awa.sinuatum.function.ecception.function.ExceptingFunction;
import com.github.cao.awa.sinuatum.function.ecception.runnable.ExceptingRunnable;
import com.github.cao.awa.sinuatum.function.ecception.supplier.ExceptingSupplier;

import java.util.function.Consumer;
import java.util.function.Function;

public class ManipulateBuilder {
    public static Manipulate<?, ?> action(ExceptingRunnable<Throwable> action) {
        return new Manipulate<>((i) -> {
            action.run();
            return null;
        });
    }

    public static <X> Manipulate<?, X> supply(ExceptingSupplier<X, Throwable> supplier) {
        return new Manipulate<>((i) -> supplier.get());
    }

    public static <X, Y> Manipulate<X, Y> make(ExceptingFunction<X, Y, Throwable> function) {
        return new Manipulate<>(function);
    }

    public static <X> Manipulate<X, ?> notNull(ExceptingConsumer<X, Throwable> consumer) {
        return new Manipulate<>((x) -> {
            if (x != null) {
                consumer.accept(x);
            }
            return null;
        });
    }

    public static <X, Y> Manipulate<X, Y> supplyWhenNotNull(ExceptingFunction<X, Y, Throwable> consumer) {
        return new Manipulate<>((x) -> {
            if (x != null) {
                return consumer.apply(x);
            }
            return null;
        });
    }

    public static Manipulate<?, ?> nulls(ExceptingRunnable<Throwable> consumer) {
        return new Manipulate<>((i) -> {
            if (i == null) {
                consumer.run();
            }
            return null;
        });
    }

    public static <Y> Manipulate<?, Y> nulls(ExceptingSupplier<Y, Throwable> consumer) {
        return new Manipulate<>((i) -> {
            if (i != null) {
                return consumer.get();
            }
            return null;
        });
    }
}
