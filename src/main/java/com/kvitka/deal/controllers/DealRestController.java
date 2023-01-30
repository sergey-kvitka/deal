package com.kvitka.deal.controllers;

import com.kvitka.deal.dtos.*;
import com.kvitka.deal.entities.Application;
import com.kvitka.deal.entities.Client;
import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.CreditStatus;
import com.kvitka.deal.jsonEntities.appliedOffer.AppliedOffer;
import com.kvitka.deal.jsonEntities.passport.Passport;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory.StatusHistoryUnit;
import com.kvitka.deal.services.impl.ApplicationServiceImpl;
import com.kvitka.deal.services.impl.ClientServiceImpl;
import com.kvitka.deal.services.impl.CreditServiceImpl;
import com.kvitka.deal.services.impl.RestTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static com.kvitka.deal.enums.ChangeType.AUTOMATIC;

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
    public void offer(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[@PutMapping(offer)] offer method called. Argument: {}", loanOfferDTO);

        Long applicationId = loanOfferDTO.getApplicationId();
        log.info("Receiving application from database by id ({})", applicationId);
        Application application = applicationService.findById(applicationId);
        log.info("Application received from database ({})", application);

        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED;
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistoryUnit(applicationStatus, AUTOMATIC));
        application.setAppliedOffer(loanOfferDTO.toAppliedOffer());
        log.info("Application's status, status history and applied offer updated, and now" +
                " application is ready to be saved to the database. Current values are: {}", application);

        application = applicationService.save(application);
        log.info("Modified application saved to the database as: {}", application);
        log.info("[@PutMapping(offer)] offer method finished.");
    }

    @PutMapping("calculate/{applicationId}")
    public void calculate(@PathVariable Long applicationId,
                          @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("[@PutMapping(calculate/{applicationId})] offer method called. Argument: {}",
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

        String calculationConveyorURL = conveyorURL + '/' + calculationConveyorEndpoint;
        log.info("Sending POST request on \"{}\" (request body: {})", calculationConveyorURL, scoringDataDTO);
        ResponseEntity<CreditDTO> creditDTOResponse = restTemplateService.postForEntity(
                calculationConveyorURL, scoringDataDTO, CreditDTO.class);
        log.info("POST request on \"{}\" sent successfully!", calculationConveyorURL);
        CreditDTO creditDTO = Objects.requireNonNull(creditDTOResponse.getBody());
        log.info("Response from POST request on \"{}\" is: {}", calculationConveyorURL, creditDTO);

        Credit credit = creditDTO.toCredit();
        credit.setCreditStatus(CreditStatus.CALCULATED);
        log.info("Credit created from credit DTO and credit status field updated ({})", credit);
        credit = creditService.save(credit);
        log.info("Created credit saved to the database as: {}", credit);

        application.setCredit(credit);
        log.info("Application's credit updated, and now application is ready to be saved to the database." +
                " Current values are: {}", application);

        applicationService.save(application);
        log.info("Application saved to the database as: {}", application);
    }
}
