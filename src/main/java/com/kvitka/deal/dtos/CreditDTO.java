package com.kvitka.deal.dtos;

import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.jsonEntities.paymentSchedule.PaymentSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreditDTO {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;

    public Credit toCredit() {
        return new Credit(null,
                amount, term,
                monthlyPayment,
                rate, psk,
                new PaymentSchedule(paymentSchedule.stream()
                        .map(PaymentScheduleElement::toPaymentScheduleUnit)
                        .collect(Collectors.toList())),
                isInsuranceEnabled,
                isSalaryClient,
                null);
    }

    public static CreditDTO from(Credit credit) {
        return new CreditDTO(
                credit.getAmount(),
                credit.getTerm(),
                credit.getMonthlyPayment(),
                credit.getRate(),
                credit.getPsk(),
                credit.getInsuranceEnable(),
                credit.getSalaryClient(),
                credit.getPaymentSchedule().getAll()
                        .stream()
                        .map(PaymentScheduleElement::from)
                        .collect(Collectors.toList()));
    }
}
