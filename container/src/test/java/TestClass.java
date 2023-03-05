import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stereotype.Component;
import util.BeanAnnotationUtils;

import java.lang.annotation.Annotation;

public class TestClass {

    @Test
    void test() {
        boolean annotationPresent = Defa.class.isAnnotationPresent(Component.class);


        Annotation[] declaredAnnotations = Defa.class.getDeclaredAnnotations();

        boolean result = false;

        for (Annotation annotation : declaredAnnotations) {
            boolean componentAnnotation = annotation.annotationType() == Component.class;
            if (componentAnnotation) {
                result =componentAnnotation;
                break;
            }
        }




        Assertions.assertEquals(true, result);
    }

    @Test
    void test2() {

        boolean result = BeanAnnotationUtils.hasAnnotation(Defa.class, Component.class);

        Assertions.assertTrue(result);
    }

    @Test
    void test3() {

        boolean result = BeanAnnotationUtils.hasAnnotation(Dff.class, Component.class);

        Assertions.assertTrue(result);
    }
}
