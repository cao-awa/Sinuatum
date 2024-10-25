package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.consumer.ExceptingConsumer;
import com.github.cao.awa.sinuatum.function.ecception.function.ExceptingFunction;
import com.github.cao.awa.sinuatum.function.ecception.runnable.ExceptingRunnable;
import com.github.cao.awa.sinuatum.function.ecception.supplier.ExceptingSupplier;

import java.lang.management.MemoryNotificationInfo;
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

    public <E extends Throwable> Manipulate<I, T> catching(Class<E> target, Consumer<E> handler) {
        this.exceptionHandlers.put(target, handler);
        return this;
    }

    public Manipulate<I, T> catching(Consumer<Throwable> handler) {
        this.genericHandler = handler;
        return this;
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
            return QuickManipulate.supplyWhenNotNull(castedEx, exceptionHandler::apply);
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
        return operateOrCreate(input, targetException, exceptionHandler);
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

    public <E extends Throwable> T operateOrCreate(I input, Class<E> targetException, BiFunction<E, I, T> exceptionHandler) {
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
            return QuickManipulate.supplyWhenNotNull(castedEx, ex -> exceptionHandler.apply(ex, input));
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
