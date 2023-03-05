package core;

import stereotype.Component;
import util.BeanAnnotationUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanAware {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private final Map<String, BeanMetadata> cacheBeanMetadata = new HashMap<>();

    public BeanAware() {
    }

    public void initialize(Class<?> startClass) {
        allPackages(startClass).forEach(it -> cacheBeanMetadata.put(it.getSimpleName(), new BeanMetadata(it)));
    }

    private List<Class<?>> allPackages(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();

        String relationPath = packageName.replace(PKG_SEPARATOR, DIR_SEPARATOR);

        URL resource = ClassLoader.getSystemClassLoader().getResource(relationPath);

        File file = new File(resource.getPath());

        List<Class<?>> classes = new ArrayList<>();

        for (File file1 : file.listFiles()) {
            classes.addAll(find(file1, relationPath));
        }

        return classes;
    }

    private List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            String replace = className.replace("/", ".");

            try {
                Class<?> clazz = Class.forName(replace);

                if (BeanAnnotationUtils.hasAnnotation(clazz, Component.class)) {
                    classes.add(clazz);
                }
            } catch (ClassNotFoundException ignore) {

            }
        }
        return classes;
    }
}
