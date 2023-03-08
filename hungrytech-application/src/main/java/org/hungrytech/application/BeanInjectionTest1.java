package org.hungrytech.application;

public class BeanInjectionTest1 {

    private HungryTechService hungryTechService;

    public BeanInjectionTest1(HungryTechService hungryTechService) {
        this.hungryTechService = hungryTechService;
    }

    public HungryTechService getHungryTechService() {
        return hungryTechService;
    }
}
