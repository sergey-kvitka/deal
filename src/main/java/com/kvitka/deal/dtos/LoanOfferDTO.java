package com.kvitka.deal.dtos;

import com.kvitka.deal.jsonEntities.appliedOffer.AppliedOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
