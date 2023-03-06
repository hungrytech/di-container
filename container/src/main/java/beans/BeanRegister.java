package beans;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanRegister {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, BeanMetadata> beanMetadataMap = new ConcurrentHashMap<>();

    public BeanRegister() {
    }

    public void registerBean(Map<String, BeanMetadata> beanMetadataMap) {
        this.beanMetadataMap.putAll(beanMetadataMap);
        this.beanMetadataMap.forEach(this::register);
    }

    private void register(String beanName, BeanMetadata beanMetadata) {
        try {
            Object singletonBean = resolveDependency(beanMetadata.getBeanType(), beanName);
            addSingleton(beanName, singletonBean);
        } catch (Exception exception) {
            throw new BeansException(
                    String.format("not created bean exception beans name : %s", beanMetadata.getBeanName()),
                    exception
            );
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        Object bean = singletonObjects.get(clazz.getSimpleName());
        return (T) bean;
    }

    @SuppressWarnings("unchecked")
    private <T> T resolveDependency(Class<T> beanType, String beanName) throws Exception {
        if (singletonObjects.containsKey(beanName)) {
            return (T) singletonObjects.get(beanName);
        }

        List<Object> constructorArguments = new ArrayList<>();

        Constructor<?> constructor = getFirstConstructor(beanType);

        for (Class<?> argumentType : constructor.getParameterTypes()) {
            Object arg = resolveDependency(argumentType, argumentType.getSimpleName());
            addSingleton(arg.getClass().getSimpleName(), arg);
            constructorArguments.add(arg);
        }

        constructor.setAccessible(true);

        return (T) constructor.newInstance(constructorArguments.toArray());
    }

    private void addSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
    }

    private Constructor<?> getFirstConstructor(Class<?> clazz) {
        Constructor<?>[] candidates = clazz.getDeclaredConstructors();

        if (candidates.length == 0) {
            throw new IllegalStateException(String.format("No constructor has been found for class %s", clazz.getName()));
        }

        return candidates[0];
    }

}
