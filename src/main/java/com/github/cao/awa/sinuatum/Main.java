package com.github.cao.awa.sinuatum;

import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import com.github.cao.awa.sinuatum.manipulate.ManipulateBuilder;
import com.github.cao.awa.sinuatum.manipulate.QuickManipulate;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        QuickManipulate.reThrow(() -> {
            System.out.println("www");
            throw new IllegalArgumentException("awa");
        }, IllegalStateException.class, ex -> {
            System.out.println("Ill state ex: ");
            return new IllegalCallerException("Rethrows", ex);
        });
    }
}
