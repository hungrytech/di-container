package core;

public class VentiApplication {

    public static void run(Class<?> startClazz) {
        new ApplicationContext(startClazz).run();
    }
}
