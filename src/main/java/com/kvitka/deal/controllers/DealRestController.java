package com.kvitka.deal.controllers;

import com.kvitka.deal.dtos.*;
import com.kvitka.deal.entities.Application;
import com.kvitka.deal.entities.Client;
import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.CreditStatus;
import com.kvitka.deal.enums.Theme;
import com.kvitka.deal.jsonEntities.appliedOffer.AppliedOffer;
import com.kvitka.deal.jsonEntities.passport.Passport;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory.StatusHistoryUnit;
import com.kvitka.deal.services.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static com.kvitka.deal.enums.ChangeType.AUTOMATIC;
import static com.kvitka.deal.enums.ChangeType.MANUAL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("deal")
public class DealRestController {

    @Value("${rest.conveyor.url}")
    private String conveyorURL;
    @Value("${rest.conveyor.endpoints.offers}")
    private String offersConveyorEndpoint;
    @Value("${rest.conveyor.endpoints.calculation}")
    private String calculationConveyorEndpoint;

    private final ApplicationServiceImpl applicationService;
    private final ClientServiceImpl clientService;
    private final CreditServiceImpl creditService;
    private final RestTemplateService restTemplateService;
    private final KafkaSendingServiceImpl kafkaSendingService;

    @PostMapping("application")
    public List<LoanOfferDTO> application(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("[@PostMapping(application)] application method called. Argument: {}", loanApplicationRequestDTO);

        ZonedDateTime timeNow = ZonedDateTime.now();
        ApplicationStatus preapproval = ApplicationStatus.PREAPPROVAL;
        Client client = Client.builder()
                .firstName(loanApplicationRequestDTO.getFirstName())
                .lastName(loanApplicationRequestDTO.getLastName())
                .middleName(loanApplicationRequestDTO.getMiddleName())
                .birthdate(loanApplicationRequestDTO.getBirthdate())
                .passport(new Passport(
                        loanApplicationRequestDTO.getPassportSeries(),
                        loanApplicationRequestDTO.getPassportNumber(), null, null))
                .email(loanApplicationRequestDTO.getEmail())
                .build();
        log.info("Client created ({})", client);

        String offersConveyorURL = conveyorURL + '/' + offersConveyorEndpoint;
        log.info("Sending POST request on \"{}\" (request body: {})", offersConveyorURL, loanApplicationRequestDTO);
        ResponseEntity<LoanOfferDTO[]> loanOffersResponse = restTemplateService.postForEntity(
                offersConveyorURL, loanApplicationRequestDTO, LoanOfferDTO[].class);
        log.info("POST request on \"{}\" sent successfully!", offersConveyorURL);
        List<LoanOfferDTO> loanOffers = List.of(Objects.requireNonNull(loanOffersResponse.getBody()));
        log.info("Response from POST request on \"{}\" is: {}", offersConveyorURL, loanOffers);

        client = clientService.save(client);
        log.info("Created client saved to the database as: {}", client);

        Application application = Application.builder()
                .client(client)
                .status(preapproval)
                .statusHistory(new StatusHistory(List.of(new StatusHistoryUnit(preapproval, timeNow, AUTOMATIC))))
                .creationDate(timeNow)
                .build();
        log.info("Application created ({})", application);
        application = applicationService.save(application);
        log.info("Created application saved to the database as: {}", application);

        Long applicationId = application.getApplicationId();
        for (LoanOfferDTO loanOfferDTO : loanOffers) {
            loanOfferDTO.setApplicationId(applicationId);
        }
        log.info("Loan offers' application ID field set to application.id ({})", applicationId);

        log.info("[@PostMapping(application)] application method returns value: {}", loanOffers);
        return loanOffers;
    }

    @PutMapping("offer")
    public ResponseEntity<?> offer(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[@PutMapping(offer)] offer method called. Argument: {}", loanOfferDTO);

        Long applicationId = loanOfferDTO.getApplicationId();
        log.info("Receiving application from database by id ({})", applicationId);
        Application application = applicationService.findById(applicationId);
        log.info("Application received from database ({})", application);

        String email = application.getClient().getEmail();
        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED;

        if (loanOfferDTO.isEmpty()) { // ! checking if all fields except applicationId are null
            log.warn("loanOfferDTO is empty. That means that the client denied the offer");
            applicationStatus = ApplicationStatus.CLIENT_DENIED;
            application.setStatus(applicationStatus);
            application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, MANUAL));
            log.warn("Application status updated ({})", applicationStatus);
            application = applicationService.save(application);
            log.warn("Application with updated status saved to the database as: {}", application);

            // ! sending message (APPLICATION_DENIED)
            EmailMessage message = new EmailMessage(email, Theme.APPLICATION_DENIED, applicationId);
            log.warn("Message created. Theme: {}; reason: client denied the offer (message: {})",
                    message.getTheme(), message);
            kafkaSendingService.sendMessage(message);

            log.warn("[@PutMapping(offer)] offer method finished.");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, AUTOMATIC));
        application.setAppliedOffer(loanOfferDTO.toAppliedOffer());
        log.info("Application's status, status history and applied offer updated, and now" +
                " application is ready to be saved to the database. Current values are: {}", application);

        application = applicationService.save(application);
        log.info("Modified application saved to the database as: {}", application);

        // ! sending message (FINISH_REGISTRATION)
        EmailMessage message = new EmailMessage(email, Theme.FINISH_REGISTRATION, applicationId);
        log.info("Message created. Theme: {}; reason: application approved so client can finish the registration " +
                "(message: {})", message.getTheme(), message);
        kafkaSendingService.sendMessage(message);

        log.info("[@PutMapping(offer)] offer method finished.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("calculate/{applicationId}")
    public void calculate(@PathVariable Long applicationId,
                          @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("[@PutMapping(calculate/{applicationId})] calculate method called. Argument: {}",
                finishRegistrationRequestDTO);

        Application application = applicationService.findById(applicationId);
        log.info("Application received from database ({})", application);
        Client client = application.getClient();
        log.info("Client received from application's field ({})", client);

        EmploymentDTO employmentDTO = finishRegistrationRequestDTO.getEmployment();
        Passport passport = client.getPassport();
        passport.setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        client.setAccount(finishRegistrationRequestDTO.getAccount());
        client.setEmployment(employmentDTO.toEmployment());
        log.info("Client's fields updated and now stored as application's field. Current values are: {}", client);

        AppliedOffer appliedOffer = application.getAppliedOffer();
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO(
                appliedOffer.getRequestedAmount(),
                appliedOffer.getTerm(),
                client.getFirstName(),
                client.getLastName(),
                client.getMiddleName(),
                client.getGender(),
                client.getBirthdate(),
                passport.getSeries(),
                passport.getNumber(),
                passport.getIssueDate(),
                passport.getIssueBranch(),
                client.getMaritalStatus(),
                client.getDependentAmount(),
                employmentDTO, client.getAccount(),
                appliedOffer.getIsInsuranceEnabled(),
                appliedOffer.getIsSalaryClient());
        log.info("Scoring data created ({})", scoringDataDTO);

        String email = client.getEmail();
        ApplicationStatus applicationStatus = ApplicationStatus.CC_APPROVED;
        CreditDTO creditDTO;

        try {
            String calculationConveyorURL = conveyorURL + '/' + calculationConveyorEndpoint;
            log.info("Sending POST request on \"{}\" (request body: {})", calculationConveyorURL, scoringDataDTO);
            ResponseEntity<CreditDTO> creditDTOResponse = restTemplateService.postForEntity(
                    calculationConveyorURL, scoringDataDTO, CreditDTO.class);
            log.info("POST request on \"{}\" sent successfully!", calculationConveyorURL);
            creditDTO = Objects.requireNonNull(creditDTOResponse.getBody());
            log.info("Response from POST request on \"{}\" is: {}", calculationConveyorURL, creditDTO);
        } catch (HttpClientErrorException e) {

            log.warn("Scoring failed. Application will be denied");
            applicationStatus = ApplicationStatus.CC_DENIED;
            application.setStatus(applicationStatus);
            application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, AUTOMATIC));
            log.warn("Application status updated ({})", applicationStatus);
            application = applicationService.save(application);
            log.warn("Application with updated status saved to the database as: {} ", application);

            // ! sending message (APPLICATION_DENIED)
            EmailMessage message = new EmailMessage(email, Theme.APPLICATION_DENIED, applicationId);
            log.warn("Message created. Theme: {}; reason: scoring failed (message: {})", message.getTheme(), message);
            kafkaSendingService.sendMessage(message);

            throw e;
        }

        Credit credit = creditDTO.toCredit();
        credit.setCreditStatus(CreditStatus.CALCULATED);
        log.info("Credit created from credit DTO and credit status field updated ({})", credit);
        credit = creditService.save(credit);
        log.info("Created credit saved to the database as: {}", credit);

        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, AUTOMATIC));
        log.info("Application status updated ({})", applicationStatus);
        application.setCredit(credit);
        log.info("Application's credit updated, and now application is ready to be saved to the database." +
                " Current values are: {}", application);

        application = applicationService.save(application);
        log.info("Application saved to the database as: {}", application);

        // ! sending message (CREATE_DOCUMENTS)
        EmailMessage message = new EmailMessage(email, Theme.CREATE_DOCUMENTS, applicationId);
        log.info("Message created. Theme: {}; reason: credit approved so the document creation can be requested " +
                "(message: {})", message.getTheme(), message);
        kafkaSendingService.sendMessage(message);

        log.info("[@PutMapping(calculate/{applicationId})] calculate method finished.");
    }
}
