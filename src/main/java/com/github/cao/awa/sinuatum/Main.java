package com.github.cao.awa.sinuatum;

import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import com.github.cao.awa.sinuatum.manipulate.RunManipulate;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        RunManipulate run = Manipulate.runLater(() -> {
            System.out.println("www");
            throw new IOException("Test exception");
        });
        System.out.println("awa");
        run.run();
    }
}
