package beans;

public interface BeanFactory {

    <T> T getBean(Class<T> requiredType);


}
