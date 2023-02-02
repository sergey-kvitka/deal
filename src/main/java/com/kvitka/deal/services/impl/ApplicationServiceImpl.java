package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Application;
import com.kvitka.deal.repositories.ApplicationRepository;
import com.kvitka.deal.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application save(Application entity) {
        log.info("Sending application to the repository to save it to the database (application = {})", entity);
        Application application = applicationRepository.save(entity);
        log.info("Application sent to the repository and successfully saved to the database as: {}", application);
        return application;
    }

    @Override
    public Application findById(Long id) {
        log.info("Sending application ID to the repository to receive it from the database (application ID = {})", id);
        Application application = applicationRepository.findById(id).orElse(null);
        if (null == application) {
            log.info("Unable to find application with ID={} in database. <null> will be returned", id);
            return null;
        }
        log.info("Application ID sent to the repository and application received and will be returned ({})",
                application);
        return application;
    }

    @Override
    public List<Application> findAll() {
        log.info("Receiving all application from database via repository");
        List<Application> applications = applicationRepository.findAll();
        log.info("Application received from database via repository ({} items)", applications.size());
        return applications;
    }
}
