package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.exception.consumer.ExceptingConsumer;
import com.github.cao.awa.sinuatum.function.exception.function.ExceptingBiFunction;
import com.github.cao.awa.sinuatum.function.exception.function.ExceptingFunction;
import com.github.cao.awa.sinuatum.function.exception.runnable.ExceptingRunnable;
import com.github.cao.awa.sinuatum.function.exception.supplier.ExceptingSupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Manipulate<M> {
    private final Map<Class<? extends Throwable>, Consumer<? extends Throwable>> exceptionHandlers = new HashMap<>();

    public <T extends Throwable> M catching(Class<T> type, Consumer<T> action) {
        this.exceptionHandlers.put(type, action);
        return beSelf();
    }

    abstract public M beSelf();

    public void handleThrowable(Throwable throwable) {
        Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
        if (handler != null) {
            handler.accept(cast(throwable));
        }
    }

    @SuppressWarnings("unchecked")
    public static <R> R cast(Object o) {
        return (R) o;
    }

    public static void run(ExceptingRunnable<Throwable> runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {

        }
    }

    public static RunManipulate runLater(ExceptingRunnable<Throwable> runnable) {
        return new RunManipulate(runnable);
    }

    public static <R> R supply(ExceptingSupplier<R, Throwable> runnable) {
        try {
            return runnable.get();
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static <R> SupplyManipulate<R> supplyLater(ExceptingSupplier<R, Throwable> supplier) {
        return new SupplyManipulate<>(supplier);
    }

    public static <I> I make(I input, ExceptingConsumer<I, Throwable> function) {
        try {
            function.accept(input);
        } catch (Throwable ignored) {

        }
        return input;
    }

    public static <I> MakeManipulate<I> makeLater(ExceptingConsumer<I, Throwable> function){
        return new MakeManipulate<>(function);
    }

    public static <I> I makeNonNull(I input, ExceptingConsumer<I, Throwable> function) {
        try {
            if (input == null) {
                return null;
            }
            function.accept(input);
        } catch (Throwable ignored) {

        }
        return input;
    }

    public static <I> MakeManipulate<I> makeNonNullLater(ExceptingConsumer<I, Throwable> function){
        return new MakeManipulate<>(p1 -> {
            if (p1 == null) {
                return;
            }
            function.accept(p1);
        });
    }


    public static <P1, R> R op(P1 p1, ExceptingFunction<P1, R, Throwable> function) {
        try {
            return function.apply(p1);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static <P1, R> Op1Manipulate<P1, R> opLater(ExceptingFunction<P1, R, Throwable> function){
        return new Op1Manipulate<>(function);
    }

    public static <P1, R> R opNonNull(P1 p1, ExceptingFunction<P1, R, Throwable> function) {
        try {
            if (p1 == null) {
                return null;
            }
            return function.apply(p1);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static <P1, R> Op1Manipulate<P1, R> opNonNullLater(ExceptingFunction<P1, R, Throwable> function){
        return new Op1Manipulate<>(p1 -> {
            if (p1 == null) {
                return null;
            }
            return function.apply(p1);
        });
    }

    public static <P1, P2, R> R op2(P1 p1, P2 p2, ExceptingBiFunction<P1, P2, R, Throwable> function) {
        try {
            return function.apply(p1, p2);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static <P1, P2, R> Op2Manipulate<P1, P2, R> op2Later(ExceptingBiFunction<P1, P2, R, Throwable> function){
        return new Op2Manipulate<>(function);
    }
}
