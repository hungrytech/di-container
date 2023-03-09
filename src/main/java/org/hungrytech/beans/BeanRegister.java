package org.hungrytech.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanRegister implements BeanFactory {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, BeanMetadata> cacheCandidateBeanMetadataMap = new ConcurrentHashMap<>();

    private final Map<Method, BeanMetadata> cacheCandidateMethodBeanMetadataMap = new ConcurrentHashMap<>();

    private final Map<String, BeanMetadata> reRegisterCandidateBeanMetadataMap = new ConcurrentHashMap<>();

    public BeanRegister() {
    }

    public void registerBean(BeanCandidatesHolder beanCandidatesHolder) {
        this.cacheCandidateBeanMetadataMap.putAll(beanCandidatesHolder.getCacheBeanMetadata());
        this.cacheCandidateMethodBeanMetadataMap.putAll(beanCandidatesHolder.getCacheMethodBeanMetadata());

        this.cacheCandidateBeanMetadataMap.forEach(this::register);
        this.cacheCandidateMethodBeanMetadataMap.forEach(this::registerCandidateMethodBeans);
        this.reRegisterCandidateBeanMetadataMap.forEach(this::register);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) singletonObjects.values()
                .stream()
                .filter(it -> clazz.isAssignableFrom(it.getClass()))
                .findFirst()
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> type) {
        return (T) singletonObjects.get(beanName);
    }

    private void register(String beanName, BeanMetadata beanMetadata) {
        try {
            Object singletonBean = resolveDependency(beanMetadata.getBeanType());

            if (singletonBean != null) {
                addSingleton(beanName, singletonBean);
            }
        } catch (Exception exception) {
            throw new BeansException(
                    String.format("not created bean exception beans name : %s", beanMetadata.getBeanName()),
                    exception
            );
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T resolveDependency(Class<T> beanType) throws Exception {

        if (isMatchCandidateMethodBean(beanType)) {
            reRegisterCandidateBeanMetadataMap.put(beanType.getSimpleName(), new BeanMetadata(beanType));
            return null;
        }


        Optional<Object> typeMatchBean = getTypeMatchBean(beanType);

        if (typeMatchBean.isPresent()) {
            return (T) typeMatchBean.get();
        }

        Constructor<?> constructor = getFirstConstructor(getCandidateBeanType(beanType));

        List<Object> constructorArguments = new ArrayList<>();

        Class<?>[] argTypes = constructor.getParameterTypes();

        for (Class<?> argumentType : argTypes) {
            if (isMatchCandidateMethodBean(argumentType)) {
                reRegisterCandidateBeanMetadataMap.put(argumentType.getSimpleName(), new BeanMetadata(argumentType));
                continue;
            }

            Object arg = resolveDependency(argumentType);
            addSingleton(arg.getClass().getSimpleName(), arg);
            constructorArguments.add(arg);
        }

        if (constructorArguments.size() != argTypes.length) {
            reRegisterCandidateBeanMetadataMap.put(beanType.getSimpleName(), new BeanMetadata(beanType));
            return null;
        }

        constructor.setAccessible(true);

        return (T) constructor.newInstance(constructorArguments.toArray());
    }

    private void registerCandidateMethodBeans(Method method, BeanMetadata beanMetadata) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            Optional<Object> typeMatchBean = getTypeMatchBean(parameterType);

            if (!typeMatchBean.isPresent()) {
                throw new BeansException(String.format("not found bean type of %s", parameterType.getName()));
            }

            parameters.add(typeMatchBean.get());
        }

        Optional<Object> configurationBean = getTypeMatchBean(method.getDeclaringClass());
        if (!configurationBean.isPresent()) {
            throw new BeansException(String.format("not found bean type of %s", configurationBean.getClass().getName()));
        }

        try {
            method.setAccessible(true);
            Object singletoneObject = method.invoke(configurationBean.get(), parameters.toArray());
            addSingleton(method.getName(), singletoneObject);

            cacheCandidateMethodBeanMetadataMap.remove(method);
        } catch (Exception exception) {
            throw new MethodBeansException(
                    String.format(
                            "bean register operation fail reason : not invoke method : %s%s",
                            beanMetadata.getBeanType(),
                            method.getName()),
                    exception
            );
        }
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

    private boolean isNotMatchCandidateBeans(Class<?> requiredType) {
        long matchCount = cacheCandidateBeanMetadataMap.values()
                .stream()
                .map(BeanMetadata::getBeanType)
                .filter(requiredType::isAssignableFrom)
                .count();

        return matchCount == 0L;
    }

    private boolean isMatchCandidateMethodBean(Class<?> requiredType) {
        long matchCount = cacheCandidateMethodBeanMetadataMap.values()
                .stream()
                .filter(it -> requiredType.isAssignableFrom(it.getBeanType()))
                .count();

        return matchCount != 0L;
    }

    private Class<?> getCandidateBeanType(Class<?> requiredType) {
        List<? extends Class<?>> candidate = cacheCandidateBeanMetadataMap.values()
                .stream()
                .map(BeanMetadata::getBeanType)
                .filter(requiredType::isAssignableFrom)
                .collect(Collectors.toList());

        return candidate.get(0);
    }

}
