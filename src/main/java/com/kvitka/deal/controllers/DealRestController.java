package com.kvitka.deal.controllers;

import com.kvitka.deal.dtos.*;
import com.kvitka.deal.entities.Application;
import com.kvitka.deal.entities.Client;
import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import com.kvitka.deal.enums.CreditStatus;
import com.kvitka.deal.jsonEntities.appliedOffer.AppliedOffer;
import com.kvitka.deal.jsonEntities.passport.Passport;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory;
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

        ResponseEntity<LoanOfferDTO[]> loanOffersResponse = restTemplateService.postForEntity(
                conveyorURL + '/' + offersConveyorEndpoint,
                loanApplicationRequestDTO, LoanOfferDTO[].class);

        client = clientService.save(client);

        Application application = Application.builder()
                .client(client)
                .status(preapproval)
                .statusHistory(new StatusHistory(List.of(
                        new StatusHistory.StatusHistoryUnit(preapproval, timeNow, ChangeType.AUTOMATIC))))
                .creationDate(timeNow)
                .build();
        application = applicationService.save(application);

        List<LoanOfferDTO> loanOffers = List.of(Objects.requireNonNull(loanOffersResponse.getBody()));
        for (LoanOfferDTO loanOfferDTO : loanOffers) {
            loanOfferDTO.setApplicationId(application.getApplicationId());
        }
        return loanOffers;
    }

    @PutMapping("offer")
    public void offer(@RequestBody LoanOfferDTO loanOfferDTO) {
        Application application = applicationService.findById(loanOfferDTO.getApplicationId());
        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED; // ?.
        application.setStatus(applicationStatus);
        application.getStatusHistory().add(new StatusHistory.StatusHistoryUnit(
                applicationStatus,
                ChangeType.AUTOMATIC));
        application.setAppliedOffer(loanOfferDTO.toAppliedOffer());
        applicationService.save(application);
    }

    @PutMapping("calculate/{applicationId}")
    public void calculate(@PathVariable Long applicationId,
                          @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        Application application = applicationService.findById(applicationId);
        EmploymentDTO employmentDTO = finishRegistrationRequestDTO.getEmployment();
        Client client = application.getClient();
        Passport passport = client.getPassport();
        passport.setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        client.setGender(finishRegistrationRequestDTO.getGender());
        client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
        client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        client.setAccount(finishRegistrationRequestDTO.getAccount());
        client.setEmployment(employmentDTO.toEmployment());

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
                employmentDTO,
                client.getAccount(),
                appliedOffer.getIsInsuranceEnabled(),
                appliedOffer.getIsSalaryClient());

        ResponseEntity<CreditDTO> creditDTOResponse = restTemplateService.postForEntity(
                conveyorURL + '/' + calculationConveyorEndpoint,
                scoringDataDTO, CreditDTO.class);

        CreditDTO creditDTO = Objects.requireNonNull(creditDTOResponse.getBody());
        Credit credit = creditDTO.toCredit();
        credit.setCreditStatus(CreditStatus.CALCULATED);
        credit = creditService.save(credit);

        application.setCredit(credit);
        applicationService.save(application);
    }
}
