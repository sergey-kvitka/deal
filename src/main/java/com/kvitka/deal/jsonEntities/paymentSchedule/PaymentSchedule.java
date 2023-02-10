package com.kvitka.deal.jsonEntities.paymentSchedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class PaymentSchedule implements Serializable {
    private List<PaymentScheduleUnit> paymentScheduleElements;

    public PaymentSchedule(List<PaymentScheduleUnit> paymentScheduleElements) {
        this.paymentScheduleElements = new ArrayList<>(paymentScheduleElements);
    }

    @SuppressWarnings("all")
    public PaymentSchedule(PaymentSchedule paymentSchedule) {
        this(paymentSchedule.getAll()
                .stream()
                .map(PaymentScheduleUnit::new)
                .collect(Collectors.toList()));
    }

    public int size() {
        return paymentScheduleElements.size();
    }

    public PaymentScheduleUnit getLast() {
        int size = size();
        if (size == 0) return null;
        return paymentScheduleElements.get(size - 1);
    }

    public PaymentScheduleUnit get(int index) {
        return paymentScheduleElements.get(index);
    }

    public void add(PaymentScheduleUnit paymentScheduleElement) {
        paymentScheduleElements.add(paymentScheduleElement);
    }

    public List<PaymentScheduleUnit> getAll() {
        return new ArrayList<>(paymentScheduleElements);
    }

    public void set(List<PaymentScheduleUnit> paymentScheduleElements) {
        this.paymentScheduleElements = new ArrayList<>(paymentScheduleElements);
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class PaymentScheduleUnit implements Serializable {
        private Integer number;
        private LocalDate date;
        private BigDecimal totalPayment;
        private BigDecimal interestPayment;
        private BigDecimal debtPayment;
        private BigDecimal remainingDebt;

        public PaymentScheduleUnit(PaymentScheduleUnit paymentScheduleUnit) {
            this.number = paymentScheduleUnit.getNumber();
            this.date = paymentScheduleUnit.getDate().plusDays(0);
            this.totalPayment = paymentScheduleUnit.getTotalPayment().add(new BigDecimal(0));
            this.interestPayment = paymentScheduleUnit.getInterestPayment().add(new BigDecimal(0));
            this.debtPayment = paymentScheduleUnit.getDebtPayment().add(new BigDecimal(0));
            this.remainingDebt = paymentScheduleUnit.getRemainingDebt().add(new BigDecimal(0));
        }
    }
}
