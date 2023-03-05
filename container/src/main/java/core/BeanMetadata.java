package core;

public class BeanMetadata {

    private final String beanName;

    private final Class<?> beanType;

    public BeanMetadata(Class<?> beanType) {
        this.beanName = beanType.getName();
        this.beanType = beanType;
    }
}
