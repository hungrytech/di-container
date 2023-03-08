package org.hungrytech.application;

import stereotype.Bean;
import stereotype.Configuration;

@Configuration
public class TaekConfig {

    @Bean
    public BeanInjectionTest1 beanInjectionTest1(HungryTechService hungryTechService) {
        return new BeanInjectionTest1(hungryTechService);
    }

    @MetaBean
    BeanInjectionTest2 beanInjectionTest2() {
        return new BeanInjectionTest2();
    }

}
