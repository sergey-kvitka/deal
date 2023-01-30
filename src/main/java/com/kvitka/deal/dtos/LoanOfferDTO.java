package com.kvitka.deal.dtos;

import com.kvitka.deal.jsonEntities.appliedOffer.AppliedOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfferDTO {
    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    public AppliedOffer toAppliedOffer() {
        log.info("Converting LoanOfferDTO to AppliedOffer (LoanOfferDTO = {})", this);
        return new AppliedOffer(
                requestedAmount,
                totalAmount,
                term,
                monthlyPayment,
                rate,
                isInsuranceEnabled,
                isSalaryClient
        );
    }

    public static LoanOfferDTO from(AppliedOffer appliedOffer) {
        log.info("Creating LoanOfferDTO by converting it from AppliedOffer (AppliedOffer = {})", appliedOffer);
        return new LoanOfferDTO(null,
                appliedOffer.getRequestedAmount(),
                appliedOffer.getTotalAmount(),
                appliedOffer.getTerm(),
                appliedOffer.getMonthlyPayment(),
                appliedOffer.getRate(),
                appliedOffer.getIsInsuranceEnabled(),
                appliedOffer.getIsSalaryClient()
        );
    }
}
