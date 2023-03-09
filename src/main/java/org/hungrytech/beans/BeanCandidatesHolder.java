package org.hungrytech.beans;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeanCandidatesHolder {

    private final Map<String, BeanMetadata> cacheBeanMetadata = new HashMap<>();

    private final Map<Method, BeanMetadata> cacheMethodBeanMetadata = new HashMap<>();

    public BeanCandidatesHolder(
            Map<String, BeanMetadata> cacheBeanMetadata,
            Map<Method, BeanMetadata> cacheMethodBeanMetadata) {
        this.cacheBeanMetadata.putAll(cacheBeanMetadata);
        this.cacheMethodBeanMetadata.putAll(cacheMethodBeanMetadata);
    }

    public Map<String, BeanMetadata> getCacheBeanMetadata() {
        return Collections.unmodifiableMap(this.cacheBeanMetadata);
    }

    public Map<Method, BeanMetadata> getCacheMethodBeanMetadata() {
        return Collections.unmodifiableMap(this.cacheMethodBeanMetadata);
    }
}
