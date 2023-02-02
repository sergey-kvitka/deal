package com.kvitka.deal.controllers;

import com.kvitka.deal.entities.Application;
import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory.StatusHistoryUnit;
import com.kvitka.deal.services.impl.ApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("deal/document")
public class DocumentRestController {

    private final ApplicationServiceImpl applicationService;
    // TODO Kafka service (private final KafkaSendingServiceImpl kafkaSendingService;)

    @PutMapping("{applicationId}/send")
    public void send(@PathVariable Long applicationId) {
        log.info("@PutMapping({applicationId}/send) send method called. Argument: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        ApplicationStatus applicationStatus = ApplicationStatus.PREPARE_DOCUMENTS;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, ChangeType.AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        applicationService.save(application);

        // TODO sending message (SEND_DOCUMENTS)

        log.info("@PutMapping({applicationId}/send) send method finished.");
    }

    @PutMapping("{applicationId}/sign")
    public void sign(@PathVariable Long applicationId) {
        log.info("@PutMapping({applicationId}/sign) sign method called. Argument: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        application.setSesCode(ThreadLocalRandom.current().nextInt(100_000, 1_000_000));
        // ! generating SES code in range [100 000; 999 999]
        log.info("Application SES code set");
        applicationService.save(application);

        // TODO sending message (SEND_SES)

        log.info("@PutMapping({applicationId}/sign) sign method finished.");
    }

    @PutMapping("{applicationId}/code")
    public void code(@PathVariable Long applicationId, @RequestBody Integer sesCodeToVerify) {
        log.info("@PutMapping({applicationId}/code) code method called. Argument: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        if (!Objects.equals(application.getSesCode(), sesCodeToVerify)) {
            log.error("SES code verification failed");
            throw new IllegalArgumentException("Invalid SES code value");
        }
        ApplicationStatus applicationStatus = ApplicationStatus.DOCUMENT_SIGNED;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, ChangeType.AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        application = applicationService.save(application);

        // TODO sending message (CREDIT_ISSUED)

        applicationStatus = ApplicationStatus.CREDIT_ISSUED;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, ChangeType.AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        applicationService.save(application);

        log.info("@PutMapping({applicationId}/code) code method finished.");
    }

}
