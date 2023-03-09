package org.hungrytech.beans;

public class DefaultApplicationContext implements ApplicationContext {

    private BeanFactory beanFactory;

    private final DefaultBeanDefinitionReader beanDefinitionReader;

    public DefaultApplicationContext(DefaultBeanDefinitionReader beanDefinitionReader) {
        this.beanDefinitionReader = beanDefinitionReader;
    }

    public void prepareContext() {
        initializeBeans();
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return this.beanFactory.getBean(type);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> type) {
        return this.beanFactory.getBean(beanName, type);
    }

    private void initializeBeans() {
        this.beanDefinitionReader.initializeBean();
        this.beanFactory = this.beanDefinitionReader.getBeanFactory();
    }
}
