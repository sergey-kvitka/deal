package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Application;
import com.kvitka.deal.repositories.ApplicationRepository;
import com.kvitka.deal.services.ApplicationService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application save(Application entity) {
        return applicationRepository.save(entity);
    }

    @Override
    public Application findById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }
}
