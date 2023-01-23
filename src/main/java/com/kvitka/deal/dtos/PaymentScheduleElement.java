package com.kvitka.deal.dtos;

import com.kvitka.deal.jsonEntities.paymentSchedule.PaymentSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    public PaymentSchedule.PaymentScheduleUnit toPaymentScheduleUnit() {
        return new PaymentSchedule.PaymentScheduleUnit(
                number,
                date,
                totalPayment,
                interestPayment,
                debtPayment,
                remainingDebt
        );
    }

    public static PaymentScheduleElement from(PaymentSchedule.PaymentScheduleUnit paymentScheduleUnit) {
        return new PaymentScheduleElement(
                paymentScheduleUnit.getNumber(),
                paymentScheduleUnit.getDate(),
                paymentScheduleUnit.getTotalPayment(),
                paymentScheduleUnit.getInterestPayment(),
                paymentScheduleUnit.getDebtPayment(),
                paymentScheduleUnit.getRemainingDebt()
        );
    }
}
