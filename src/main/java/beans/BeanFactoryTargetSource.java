package beans;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactoryTargetSource {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private final Class<?> startClass;

    private final Map<String, BeanMetadata> cacheBeanMetadata = new HashMap<>();

    private final Map<Method, BeanMetadata> cacheMethodBeanMetadata = new HashMap<>();

    public BeanFactoryTargetSource(Class<?> startClass) {
        this.startClass = startClass;
    }

    public void initialize() {
        allPackages(this.startClass);
    }

    private void allPackages(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();

        String relationPath = packageName.replace(PKG_SEPARATOR, DIR_SEPARATOR);

        URL resource = ClassLoader.getSystemClassLoader().getResource(relationPath);

        File resourecs = new File(resource.getPath());

        List<Class<?>> classes = new ArrayList<>();

        File[] files = resourecs.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            List<Class<?>> findResults = find(file, relationPath);

            if (findResults == null) {
                continue;
            }

            classes.addAll(findResults);
        }

        classes.forEach(it -> cacheBeanMetadata.put(it.getSimpleName(), new BeanMetadata(it)));
    }

    private List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files == null) {
                return null;
            }

            for (File child : files) {
                List<Class<?>> findResults = find(child, resource);

                if (findResults == null) {
                    return null;
                }

                classes.addAll(findResults);
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String replace = resource.substring(0, endIndex).replace("/", ".");

            try {
                Class<?> target = Class.forName(replace);

                if (target.isAnnotation()) {
                    return classes;

                } else if (BeanAnnotationHelper.hasConfigurationAnnotation(target)) {
                    cacheMethodBeanCandidate(target);
                    classes.add(target);
                } else {
                    if (BeanAnnotationHelper.hasComponentAnnotation(target)) {
                        classes.add(target);
                    }
                }
            } catch (ClassNotFoundException ignore) {

            }
        }
        return classes;
    }

    private void cacheMethodBeanCandidate(Class<?> configurationClass) {
        Method[] declaredMethods = configurationClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (BeanAnnotationHelper.hasBeanAnnotation(method)) {
                Class<?> returnType = method.getReturnType();
                cacheMethodBeanMetadata.put(method, new BeanMetadata(returnType));
            }
        }
    }

    public BeanCandidatesHolder getBeanCandidatesHolder() {
        return new BeanCandidatesHolder(cacheBeanMetadata, cacheMethodBeanMetadata);
    }
}
