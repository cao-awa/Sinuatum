package com.github.cao.awa.sinuatum.manipulate;

import com.github.cao.awa.sinuatum.function.ecception.function.ExceptingBiFunction;
import com.github.cao.awa.sinuatum.function.ecception.function.ExceptingFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Op2Manipulate<P1, P2, R> extends Manipulate<Op2Manipulate<P1, P2, R>> {
    private final ExceptingBiFunction<P1, P2, R, Throwable> function;

    public Op2Manipulate(ExceptingBiFunction<P1, P2,R, Throwable> function) {
        this.function = function;
    }

    @Override
    public Op2Manipulate<P1, P2, R> beSelf() {
        return this;
    }

    public R op(P1 p1, P2 p2) {
        try {
            return this.function.apply(p1, p2);
        } catch (Throwable throwable) {
            handleThrowable(throwable);
            return null;
        }
    }

    public R opOr(P1 p1, P2 p2, @NotNull R defaultValue) {
        R result = op(p1, p2);
        return result == null ? defaultValue : result;
    }

    public R opOr(P1 p1, P2 p2, @NotNull BiFunction<P1, P2, R> function) {
        R result = op(p1, p2);
        return result == null ? function.apply(p1, p2) : result;
    }
}
