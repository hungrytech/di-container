package org.hungrytech;

import beans.ApplicationContext;
import core.VentiApplication;
import org.hungrytech.application.BeanInjectionTest2;

public class HungryTechApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = VentiApplication.run(HungryTechApplication.class);

        BeanInjectionTest2 beanInjectionTest2 = applicationContext.getBean("beanInjectionTest2", BeanInjectionTest2.class);

        System.out.println(beanInjectionTest2);
    }
}
