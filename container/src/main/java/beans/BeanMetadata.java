package beans;

import java.lang.reflect.Parameter;

public class BeanMetadata {

    private final String beanName;

    private final Class<?> beanType;

    private final Parameter[] parameters;

    public BeanMetadata(Class<?> beanType) {
        this(beanType, null);
    }

    public BeanMetadata(Class<?> beanType, Parameter[] parameters) {
        this.beanName = beanType.getSimpleName();
        this.beanType = beanType;
        this.parameters = parameters;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
