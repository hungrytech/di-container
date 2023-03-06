package core;

import beans.BeanAware;
import beans.BeanRegister;

public class VentiApplication {

    private final Class<?> mainApplicationClass;

    public VentiApplication(Class<?> mainApplicationClass) {
        this.mainApplicationClass = mainApplicationClass;
    }

    public ApplicationContext run() {
        ApplicationContext applicationContext = new ApplicationContext();

        BeanAware beanAware = new BeanAware();
        beanAware.initialize(mainApplicationClass);

        BeanRegister beanRegister = applicationContext.getBeanFactory();
        beanRegister.registerBean(beanAware.getBeanMetadatas());

        return applicationContext;
    }

    public static ApplicationContext run(Class<?> mainApplicationClass) {
        return new VentiApplication(mainApplicationClass).run();
    }
}
