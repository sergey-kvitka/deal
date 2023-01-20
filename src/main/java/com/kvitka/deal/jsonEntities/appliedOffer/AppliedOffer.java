package com.kvitka.deal.jsonEntities.appliedOffer;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppliedOffer implements Serializable {
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppliedOffer that = (AppliedOffer) o;
        return Objects.equals(requestedAmount, that.requestedAmount)
                && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(term, that.term)
                && Objects.equals(monthlyPayment, that.monthlyPayment) && Objects.equals(rate, that.rate)
                && Objects.equals(isInsuranceEnabled, that.isInsuranceEnabled)
                && Objects.equals(isSalaryClient, that.isSalaryClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedAmount, totalAmount, term,
                monthlyPayment, rate, isInsuranceEnabled, isSalaryClient);
    }
}
