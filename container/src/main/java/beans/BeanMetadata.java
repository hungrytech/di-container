package beans;

public class BeanMetadata {

    private final String beanName;

    private final Class<?> beanType;

    public BeanMetadata(Class<?> beanType) {
        this.beanName = beanType.getSimpleName();
        this.beanType = beanType;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
