package com.metaphore.war3keybinder.utils;

import java.io.File;

public class ResourceUtils {
    public static File get(String path) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}
