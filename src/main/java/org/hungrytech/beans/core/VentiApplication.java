package org.hungrytech.beans.core;

import org.hungrytech.beans.ApplicationContext;
import org.hungrytech.beans.BeanFactoryTargetSource;
import org.hungrytech.beans.BeanRegister;
import org.hungrytech.beans.DefaultApplicationContext;
import org.hungrytech.beans.DefaultBeanDefinitionReader;

public class VentiApplication {

    private final Class<?> mainApplicationClass;

    public VentiApplication(Class<?> mainApplicationClass) {
        this.mainApplicationClass = mainApplicationClass;
    }

    public ApplicationContext run() {

        BeanFactoryTargetSource beanFactoryTargetSource = new BeanFactoryTargetSource(mainApplicationClass);

        DefaultBeanDefinitionReader defaultBeanDefinitionReader = new DefaultBeanDefinitionReader(
            new BeanRegister(),
            beanFactoryTargetSource
        );

        DefaultApplicationContext defaultApplicationContext = new DefaultApplicationContext(
                defaultBeanDefinitionReader
        );

        defaultApplicationContext.prepareContext();

        return defaultApplicationContext;
    }

    public static ApplicationContext run(Class<?> mainApplicationClass) {
        return new VentiApplication(mainApplicationClass).run();
    }
}
