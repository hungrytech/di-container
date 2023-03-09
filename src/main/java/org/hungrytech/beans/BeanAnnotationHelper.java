package org.hungrytech.beans;

import org.hungrytech.beans.stereotype.Bean;
import org.hungrytech.beans.stereotype.Component;
import org.hungrytech.beans.stereotype.Configuration;
import org.hungrytech.beans.util.BeanAnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BeanAnnotationHelper {

    public static boolean hasBeanAnnotation(Method method) {
        if (method.isAnnotationPresent(Bean.class)) {
            return true;
        }

        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            return BeanAnnotationUtils.hasAnnotation(annotation.annotationType(), Bean.class);
        }

        return false;
    }

    public static boolean hasComponentAnnotation(Class<?> requiredClass) {
        return BeanAnnotationUtils.hasAnnotation(requiredClass, Component.class);
    }

    public static boolean hasConfigurationAnnotation(Class<?> requiredClass) {
        return BeanAnnotationUtils.hasAnnotation(requiredClass, Configuration.class);
    }
}
