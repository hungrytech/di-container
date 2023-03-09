package core;

import beans.*;

public class VentiApplication {

    private final Class<?> mainApplicationClass;

    public VentiApplication(Class<?> mainApplicationClass) {
        this.mainApplicationClass = mainApplicationClass;
    }

    public DefaultApplicationContext run() {

        BeanFactoryTargetSource beanFactoryTargetSource = new BeanFactoryTargetSource(mainApplicationClass);

        DefaultBeanDefinitionReader defaultBeanDefinitionReader = new DefaultBeanDefinitionReader(
                new BeanRegister(),
                beanFactoryTargetSource
        );

        DefaultApplicationContext defaultApplicationContext = new DefaultApplicationContext(defaultBeanDefinitionReader);

        defaultApplicationContext.prepareContext();

        return defaultApplicationContext;
    }

    public static DefaultApplicationContext run(Class<?> mainApplicationClass) {
        return new VentiApplication(mainApplicationClass).run();
    }
}
