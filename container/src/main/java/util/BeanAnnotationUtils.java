package util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

public class BeanAnnotationUtils {

    private static final Set<Class<? extends Annotation>> notSearchAnnotation = new HashSet<>();

    static {
        notSearchAnnotation.add(Retention.class);
        notSearchAnnotation.add(Target.class);
        notSearchAnnotation.add(Documented.class);
    }

    public static <A extends Annotation> boolean hasAnnotation(Class<?> target, Class<A> annotationType) {
        Annotation[] declaredAnnotations = target.getDeclaredAnnotations();

        for (Annotation annotation : declaredAnnotations) {
            if (annotation.annotationType() == annotationType) {
                return true;
            }

            if (findAnnotation(annotation.annotationType(), annotationType)) {
                return true;
            }
        }

        return false;
    }

    private static boolean findAnnotation(
            Class<? extends Annotation> superClass,
            Class<? extends Annotation> annotationType) {
        if (superClass.isAnnotationPresent(annotationType)) {
            return true;
        }

        for (Annotation annotation : superClass.getDeclaredAnnotations()) {
            if (notSearchAnnotation.contains(annotation.annotationType())) {
                continue;
            }

            return findAnnotation(annotation.annotationType(), annotationType);
        }

        return false;
    }

}
