package com.github.cao.awa.sinuatum.resource.loader;

import java.io.File;
import java.io.InputStream;

public class ResourceLoader {
    public static InputStream get(String resource) {
        return ResourceLoader.class.getClassLoader()
                .getResourceAsStream(resource);
    }

    public static File asFile(String resource) {
        return new File(String.valueOf(ResourceLoader.class.getResource(resource)));
    }
}

