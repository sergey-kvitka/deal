package com.kvitka.deal.dtos;

import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.entities.PaymentSchedule;
import com.kvitka.deal.entities.PaymentScheduleElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
                new PaymentSchedule(paymentSchedule),
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
                credit.getPaymentSchedule().getList());
    }
}
