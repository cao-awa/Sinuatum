package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.exception.function.ExceptingFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Op1Manipulate<P1, R> extends Manipulate<Op1Manipulate<P1, R>> {
    private final ExceptingFunction<P1, R, Throwable> function;

    public Op1Manipulate(ExceptingFunction<P1,R, Throwable> function) {
        this.function = function;
    }

    @Override
    public Op1Manipulate<P1, R> beSelf() {
        return this;
    }

    public R op(P1 p1) {
        try {
            return this.function.apply(p1);
        } catch (Throwable throwable) {
            handleThrowable(throwable);
            return null;
        }
    }

    public R opOr(P1 p1, @NotNull R defaultValue) {
        R result = op(p1);
        return result == null ? defaultValue : result;
    }

    public R opOr(P1 p1, @NotNull Function<P1, R> function) {
        R result = op(p1);
        return result == null ? function.apply(p1) : result;
    }
}
