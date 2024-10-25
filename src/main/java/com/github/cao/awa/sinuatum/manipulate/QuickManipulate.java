package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.consumer.ExceptingConsumer;
import com.github.cao.awa.sinuatum.function.ecception.function.ExceptingFunction;
import com.github.cao.awa.sinuatum.function.ecception.runnable.ExceptingRunnable;
import com.github.cao.awa.sinuatum.function.ecception.supplier.ExceptingSupplier;

import java.util.function.Consumer;
import java.util.function.Function;

public class QuickManipulate {
    public static <X> void notNull(X target, ExceptingConsumer<X, Throwable> consumer) {
        new Manipulate<>((X x) -> {
            if (x != null) {
                consumer.accept(x);
            }
            return null;
        }).operate(target);
    }

    public static <X, Y> Y supplyWhenNotNull(X target, ExceptingFunction<X, Y, Throwable> creator) {
        return new Manipulate<>((X x) -> {
            if (x != null) {
                return creator.apply(x);
            }
            return null;
        }).operate(target);
    }

    public static <X> void nulls(X target, ExceptingRunnable<Throwable> consumer) {
        new Manipulate<>((X i) -> {
            if (i == null) {
                consumer.run();
            }
            return null;
        }).operate(target);
    }

    public static <X> X operation(X target, ExceptingConsumer<X, Throwable> action) {
        new Manipulate<>((X i) -> {
            action.accept(i);
            return null;
        }).operate(target);
        return target;
    }

    public static <E extends Throwable, R extends Throwable> void reThrow(ExceptingRunnable<E> runnable, Class<E> specifiedType, Function<E, R> makeException, Function<Throwable, R> whenOther) throws R {
        new Manipulate<>((i) -> {
            runnable.run();
            return null;
        }).reThrow(specifiedType, makeException, whenOther);
    }

    public static <E extends Throwable, R extends Throwable> void reThrow(ExceptingRunnable<E> runnable, Class<E> specifiedType, Function<E, R> makeException, Consumer<Throwable> whenOther) throws R {
        new Manipulate<>((i) -> {
            runnable.run();
            return null;
        }).reThrow(specifiedType, makeException, whenOther);
    }

    public static <E extends Throwable, R extends Throwable> void reThrow(ExceptingRunnable<E> runnable, Class<E> specifiedType, Function<E, R> makeException) throws R {
        new Manipulate<>((i) -> {
            runnable.run();
            return null;
        }).reThrow(specifiedType, makeException);
    }

    public static <E extends Throwable, R extends Throwable, X> X reThrow(ExceptingSupplier<X, E> runnable, Class<E> specifiedType, Function<E, R> makeException, Function<Throwable, R> whenOther) throws R {
        return new Manipulate<>((i) -> runnable.get()).reThrow(specifiedType, makeException, whenOther);
    }

    public static <E extends Throwable, R extends Throwable, X> X reThrow(ExceptingSupplier<X, E> runnable, Class<E> specifiedType, Function<E, R> makeException, Consumer<Throwable> whenOther) throws R {
        return new Manipulate<>((i) -> runnable.get()).reThrow(specifiedType, makeException, whenOther);
    }

    public static <E extends Throwable, R extends Throwable, X> X reThrow(ExceptingSupplier<X, E> runnable, Class<E> specifiedType, Function<E, R> makeException) throws R {
        return new Manipulate<>((i) -> runnable.get()).reThrow(specifiedType, makeException);
    }
}
