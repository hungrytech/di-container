package org.hungrytech.beans;

public class DefaultBeanDefinitionReader {

    private final BeanRegister beanRegister;

    private final BeanFactoryTargetSource beanFactoryTargetSource;

    public DefaultBeanDefinitionReader(BeanRegister beanRegister, BeanFactoryTargetSource beanFactoryTargetSource) {
        this.beanRegister = beanRegister;
        this.beanFactoryTargetSource = beanFactoryTargetSource;
    }

    public void initializeBean() {
        beanFactoryTargetSource.initialize();
        beanRegister.registerBean(beanFactoryTargetSource.getBeanCandidatesHolder());
    }

    public BeanFactory getBeanFactory() {
        return this.beanRegister;
    }
}
