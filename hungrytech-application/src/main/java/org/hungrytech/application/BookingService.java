package org.hungrytech.application;

import org.hungrytech.repository.HungryTechRepository;
import stereotype.Service;

@Service
public class BookingService {

    private final HungryTechRepository hungryTechRepository;

    public BookingService(HungryTechRepository hungryTechRepository) {
        this.hungryTechRepository = hungryTechRepository;
    }

    public HungryTechRepository getHungryTechRepository() {
        return hungryTechRepository;
    }
}
