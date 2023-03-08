package org.hungrytech.application;

import org.hungrytech.repository.HungryTechRepositoryImpl;
import stereotype.Service;

@Service
public class BoService {

    private final AbstractHungryTechRepository hungryTechRepository;

    public BoService(AbstractHungryTechRepository hungryTechRepository) {
        this.hungryTechRepository = hungryTechRepository;
    }

    public AbstractHungryTechRepository getHungryTechRepository() {
        return hungryTechRepository;
    }
}
