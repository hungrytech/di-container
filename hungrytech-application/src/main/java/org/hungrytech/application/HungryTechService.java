package org.hungrytech.application;

import stereotype.Service;

@Service
public class HungryTechService {

    private final HungryTechRepository hungryTechRepository;

    public HungryTechService(HungryTechRepository hungryTechRepository) {
        this.hungryTechRepository = hungryTechRepository;
    }

    public HungryTechRepository getHungryTechRepository() {
        return hungryTechRepository;
    }

}
