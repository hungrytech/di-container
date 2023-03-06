package core;

import beans.BeanRegister;

public class ApplicationContext {

    private final BeanRegister beanRegister;

    public ApplicationContext() {
        this.beanRegister = new BeanRegister();
    }

    public BeanRegister getBeanFactory() {
        return this.beanRegister;
    }

    public <T> T getBean(Class<T> clazz) {
        return this.beanRegister.getBean(clazz);
    }
}
