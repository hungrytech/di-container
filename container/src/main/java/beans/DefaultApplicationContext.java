package beans;

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
    public <T> T getBean(Class<T> clazz) {
        return this.beanFactory.getBean(clazz);
    }

    private void initializeBeans() {
        this.beanDefinitionReader.initializeBean();
        this.beanFactory = this.beanDefinitionReader.getBeanFactory();
    }
}
