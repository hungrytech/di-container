package beans;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanRegister implements BeanFactory {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, BeanMetadata> cacheCandidateBeanMetadataMap = new ConcurrentHashMap<>();

    public BeanRegister() {
    }

    public void registerBean(Map<String, BeanMetadata> beanMetadataMap) {
        this.cacheCandidateBeanMetadataMap.putAll(beanMetadataMap);
        this.cacheCandidateBeanMetadataMap.forEach(this::register);
    }

    private void register(String beanName, BeanMetadata beanMetadata) {
        try {
            Object singletonBean = resolveDependency(beanMetadata.getBeanType());
            addSingleton(beanName, singletonBean);
        } catch (Exception exception) {
            throw new BeansException(
                    String.format("not created bean exception beans name : %s", beanMetadata.getBeanName()),
                    exception
            );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        Object bean = singletonObjects.get(clazz.getSimpleName());
        return (T) bean;
    }

    @SuppressWarnings("unchecked")
    private <T> T resolveDependency(Class<T> beanType) throws Exception {
        Optional<Object> typeMatchBean = getTypeMatchBean(beanType);

        if (typeMatchBean.isPresent()) {
            return (T) typeMatchBean.get();
        }

        Constructor<?> constructor = getFirstConstructor(getCandidateBeanType(beanType));

        List<Object> constructorArguments = new ArrayList<>();

        for (Class<?> argumentType : constructor.getParameterTypes()) {
            Object arg = resolveDependency(argumentType);
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

    private Optional<Object> getTypeMatchBean(Class<?> requestType) {
        if (isNotMatchCandidateBeans(requestType)) {
            throw new BeansException(String.format("not find bean candidate type of %s", requestType.getName()));
        }

        List<? extends Class<?>> candidate = this.singletonObjects.values().stream()
                .map(Object::getClass)
                .filter(requestType::isAssignableFrom)
                .collect(Collectors.toList());

        return candidate.isEmpty() ? Optional.empty() :
                Optional.of(singletonObjects.get(candidate.get(0).getSimpleName()));
    }

    private boolean isNotMatchCandidateBeans(Class<?> requestType) {
        long matchCount = cacheCandidateBeanMetadataMap.values()
                .stream()
                .map(BeanMetadata::getBeanType)
                .filter(requestType::isAssignableFrom)
                .count();

        return matchCount == 0L;
    }

    private Class<?> getCandidateBeanType(Class<?> requestType) {
        List<? extends Class<?>> candidate = cacheCandidateBeanMetadataMap.values()
                .stream()
                .map(BeanMetadata::getBeanType)
                .filter(requestType::isAssignableFrom)
                .collect(Collectors.toList());

        return candidate.get(0);
    }
}
