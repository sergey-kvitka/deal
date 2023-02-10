package com.kvitka.deal.dtos;

import com.kvitka.deal.jsonEntities.paymentSchedule.PaymentSchedule.PaymentScheduleUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    public PaymentScheduleUnit toPaymentScheduleUnit() {
        log.info("Converting PaymentScheduleElement (DTO) to PaymentScheduleUnit (PaymentScheduleElement = {})", this);
        return new PaymentScheduleUnit(
                number,
                date,
                totalPayment,
                interestPayment,
                debtPayment,
                remainingDebt
        );
    }

    public static PaymentScheduleElement from(PaymentScheduleUnit paymentScheduleUnit) {
        log.info("Creating PaymentScheduleElement (DTO) by converting it from PaymentScheduleUnit " +
                "(PaymentScheduleUnit = {})", paymentScheduleUnit);
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
