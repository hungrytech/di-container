package core;

public class ApplicationContext {

    private final Class<?> startUp;

    public ApplicationContext(Class<?> startUp) {
        this.startUp = startUp;
    }

    public void run() {
        BeanAware beanAware = new BeanAware();
        beanAware.initialize(startUp);


    }
}
