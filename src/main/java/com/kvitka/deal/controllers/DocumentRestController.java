package com.kvitka.deal.controllers;

import com.kvitka.deal.dtos.EmailMessage;
import com.kvitka.deal.entities.Application;
import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import com.kvitka.deal.enums.CreditStatus;
import com.kvitka.deal.enums.Theme;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory.StatusHistoryUnit;
import com.kvitka.deal.services.impl.ApplicationServiceImpl;
import com.kvitka.deal.services.impl.KafkaSendingServiceImpl;
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
    private final KafkaSendingServiceImpl kafkaSendingService;

    @PutMapping("{applicationId}/send")
    public void send(@PathVariable Long applicationId) {
        log.info("@PutMapping({applicationId}/send) send method called. Argument: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        ApplicationStatus applicationStatus = ApplicationStatus.PREPARE_DOCUMENTS;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, ChangeType.AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        applicationService.save(application);

        // ! sending message (SEND_DOCUMENTS)
        String email = application.getClient().getEmail();
        EmailMessage message = new EmailMessage(email, Theme.SEND_DOCUMENTS, applicationId);
        log.info("Message created. Theme: {}; reason: documents can be created and sent to the client (message: {})",
                message.getTheme(), message);
        kafkaSendingService.sendMessage(message);

        log.info("@PutMapping({applicationId}/send) send method finished.");
    }

    @PutMapping("{applicationId}/sign")
    public void sign(@PathVariable Long applicationId) {
        log.info("@PutMapping({applicationId}/sign) sign method called. Argument: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        // ! generating SES code in range [100 000; 999 999]
        int sesCode = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        application.setSesCode(sesCode);
        log.info("Application SES code set");
        applicationService.save(application);

        // ! sending message (SEND_SES)
        String email = application.getClient().getEmail();
        EmailMessage message = new EmailMessage(email, Theme.SEND_SES, applicationId, String.valueOf(sesCode));
        log.info("Message created. Theme: {}; reason: SES code generated and can be sent to the client (message: {})",
                message.getTheme(), message);
        kafkaSendingService.sendMessage(message);

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

        applicationStatus = ApplicationStatus.CREDIT_ISSUED;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, ChangeType.AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        CreditStatus creditStatus = CreditStatus.ISSUED;
        application.getCredit().setCreditStatus(creditStatus);
        log.info("Credit status (in application.credit) updated ({})", creditStatus);
        applicationService.save(application);

        // ! sending message (CREDIT_ISSUED)
        String email = application.getClient().getEmail();
        EmailMessage message = new EmailMessage(email, Theme.CREDIT_ISSUED, applicationId);
        log.info("Message created. Theme: {}; reason: SES code verified and credit issued successfully (message: {})",
                message.getTheme(), message);
        kafkaSendingService.sendMessage(message);

        log.info("@PutMapping({applicationId}/code) code method finished.");
    }
}
