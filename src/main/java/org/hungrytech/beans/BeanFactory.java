package org.hungrytech.beans;

public interface BeanFactory {

    <T> T getBean(Class<T> requiredType);

    <T> T getBean(String beanName, Class<T> type);
}
