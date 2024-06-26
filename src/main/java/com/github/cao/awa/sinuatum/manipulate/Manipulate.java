package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.consumer.ExceptingConsumer;
import com.github.cao.awa.sinuatum.function.ecception.function.ExceptingFunction;
import com.github.cao.awa.sinuatum.function.ecception.runnable.ExceptingRunnable;
import com.github.cao.awa.sinuatum.function.ecception.supplier.ExceptingSupplier;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Manipulate<I, T> {
    private ExceptingFunction<I, T, Throwable> action;
    private Map<Class<? extends Throwable>, Consumer<? extends Throwable>> exceptionHandlers = new HashMap<>();
    private Consumer<Throwable> genericHandler;

    public Manipulate(ExceptingFunction<I, T, Throwable> action) {
        this.action = action;
    }

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

    public static <X> void nulls(X target, ExceptingRunnable<Throwable> consumer) {
        new Manipulate<>((X i) -> {
            if (i == null) {
                consumer.run();
            }
            return null;
        }).operate(target);
    }

    public static <Y> Manipulate<?, Y> nulls(ExceptingSupplier<Y, Throwable> consumer) {
        return new Manipulate<>((i) -> {
            if (i != null) {
                return consumer.get();
            }
            return null;
        });
    }

    public static <X> X operation(X target, ExceptingConsumer<X, Throwable> action) {
        new Manipulate<>((X i) -> {
            action.accept(i);
            return null;
        }).operate(target);
        return target;
    }

    public <E extends Throwable> Manipulate<I, T> catching(Class<E> target, Consumer<E> handler) {
        this.exceptionHandlers.put(target, handler);
        return this;
    }

    public Manipulate<I, T> catching(Consumer<Throwable> handler) {
        this.genericHandler = handler;
        return this;
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

    public <E extends Throwable, R extends Throwable> T reThrow(Class<E> specifiedType, Function<E, R> makeException, Function<Throwable, R> whenOther) throws R {
        try {
            return this.action.apply(null);
        } catch (Throwable e) {
            E specifiedEx;
            try {
                specifiedEx = specifiedType.cast(e);
            } catch (Exception ignored) {
                throw whenOther.apply(e);
            }
            throw makeException.apply(specifiedEx);
        }
    }

    public <E extends Throwable, R extends Throwable> T reThrow(Class<E> specifiedType, Function<E, R> makeException, Consumer<Throwable> whenOther) throws R {
        try {
            return this.action.apply(null);
        } catch (Throwable e) {
            E specifiedEx;
            try {
                specifiedEx = specifiedType.cast(e);
            } catch (Exception ignored) {
                whenOther.accept(e);
                return null;
            }
            throw makeException.apply(specifiedEx);
        }
    }

    public <E extends Throwable, R extends Throwable> T reThrow(Class<E> specifiedType, Function<E, R> makeException) throws R {
        try {
            return this.action.apply(null);
        } catch (Throwable e) {
            E specifiedEx;
            try {
                specifiedEx = specifiedType.cast(e);
            } catch (Exception ignored) {
                return null;
            }
            throw makeException.apply(specifiedEx);
        }
    }

    public void execute() {
        try {
            this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
        }
    }

    public T get() {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
        }

        return null;
    }

    public T get(Function<Throwable, T> exceptionHandler) {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
            return exceptionHandler.apply(throwable);
        }
    }

    public <E extends Throwable> T get(Class<E> targetException, Function<E, T> exceptionHandler) {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
            E castedEx = cast(throwable);
            return supplyWhenNotNull(castedEx, exceptionHandler::apply);
        }
    }

    public T getOrCreate(Supplier<T> creator) {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
        }

        return creator.get();
    }

    public T getOrCreate(Function<Throwable, T> creator) {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }

            return creator.apply(throwable);
        }
    }

    public <E extends Throwable> T getOrCreate(Class<E> targetException, Function<E, T> creator) {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }

            return creator.apply(cast(throwable));
        }
    }

    public T getOrDefault(T defaultValue) {
        try {
            return this.action.apply(null);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
        }

        return defaultValue;
    }

    public T operate(I input) {
        try {
            return this.action.apply(input);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
        }

        return null;
    }

    public T operate(I input, BiFunction<Throwable, I, T> exceptionHandler) {
        try {
            return this.action.apply(input);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }

            return exceptionHandler.apply(throwable, input);
        }
    }

    public <E extends Throwable> T operate(I input, Class<E> targetException, BiFunction<E, I, T> exceptionHandler) {
        try {
            return this.action.apply(input);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
            E castedEx = cast(throwable);
            return supplyWhenNotNull(castedEx, ex -> exceptionHandler.apply(ex, input));
        }
    }

    public T operateOrCreate(I input, Function<I, T> creator) {
        try {
            return this.action.apply(input);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }

            return creator.apply(input);
        }
    }

    public <E extends Throwable> T operateOrCreate(I input, Class<E> targetException, BiFunction<E, I, T> creator) {
        try {
            return this.action.apply(input);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
            E castedEx = cast(throwable);
            return supplyWhenNotNull(castedEx, ex -> creator.apply(ex, input));
        }
    }

    public T operateOrDefault(I input, T defaultValue) {
        try {
            return this.action.apply(input);
        } catch (Throwable throwable) {
            Consumer<? extends Throwable> handler = this.exceptionHandlers.get(throwable.getClass());
            if (handler != null) {
                handler.accept(cast(throwable));
            }
            if (this.genericHandler != null) {
                this.genericHandler.accept(throwable);
            }
        }

        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static <R> R cast(Object object) {
        try {
            return (R) object;
        } catch (Throwable ignored) {
            return null;
        }
    }
}
