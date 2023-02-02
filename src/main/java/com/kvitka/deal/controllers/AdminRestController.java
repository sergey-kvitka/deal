package com.kvitka.deal.controllers;

import com.kvitka.deal.entities.Application;
import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory.StatusHistoryUnit;
import com.kvitka.deal.services.impl.ApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("deal/admin")
public class AdminRestController {

    private final ApplicationServiceImpl applicationService;

    @GetMapping("application")
    public List<Application> getAllApplications() {
        log.info("@GetMapping(application) getAllApplications method called");
        List<Application> applications = applicationService.findAll();
        log.info("@GetMapping(application) getAllApplications method returns list of applications ({} items)",
                applications.size());
        return applications;
    }

    @GetMapping("application/{applicationId}")
    public Application getApplicationById(@PathVariable Long applicationId) {
        log.info("@GetMapping(application/{applicationId}) getApplicationById method called. Argument: {}",
                applicationId);
        Application application = applicationService.findById(applicationId);
        log.info("@GetMapping(application/{applicationId}) getApplicationById method returns value: {}", application);
        return application;
    }

    @PutMapping("application/{applicationId}/status")
    public void setDocumentCreatedStatus(@PathVariable Long applicationId) {
        log.info("@GetMapping(application/{applicationId}/status) setDocumentCreatedStatus method called. Argument: {}",
                applicationId);
        Application application = applicationService.findById(applicationId);
        ApplicationStatus applicationStatus = ApplicationStatus.DOCUMENT_CREATED;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, ChangeType.AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        applicationService.save(application);
        log.info("@GetMapping(application/{applicationId}/status) setDocumentCreatedStatus method finished.");
    }

}
