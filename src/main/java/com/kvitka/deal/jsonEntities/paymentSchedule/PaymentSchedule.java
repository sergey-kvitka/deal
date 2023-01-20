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
    private List<PaymentScheduleElement> paymentScheduleElements;

    public PaymentSchedule(List<PaymentScheduleElement> paymentScheduleElements) {
        this.paymentScheduleElements = new ArrayList<>(paymentScheduleElements);
    }

    @SuppressWarnings("all")
    public PaymentSchedule(PaymentSchedule paymentSchedule) {
        this(paymentSchedule.getAll()
                .stream()
                .map(PaymentScheduleElement::new)
                .collect(Collectors.toList()));
    }

    public int size() {
        return paymentScheduleElements.size();
    }

    public PaymentScheduleElement getLast() {
        int size = size();
        if (size == 0) return null;
        return paymentScheduleElements.get(size - 1);
    }

    public PaymentScheduleElement get(int index) {
        return paymentScheduleElements.get(index);
    }

    public void add(PaymentScheduleElement paymentScheduleElement) {
        paymentScheduleElements.add(paymentScheduleElement);
    }

    public List<PaymentScheduleElement> getAll() {
        return new ArrayList<>(paymentScheduleElements);
    }

    public void set(List<PaymentScheduleElement> paymentScheduleElements) {
        this.paymentScheduleElements = new ArrayList<>(paymentScheduleElements);
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class PaymentScheduleElement implements Serializable {
        private Integer number;
        private LocalDate date;
        private BigDecimal totalPayment;
        private BigDecimal interestPayment;
        private BigDecimal debtPayment;
        private BigDecimal remainingDebt;

        public PaymentScheduleElement(PaymentScheduleElement paymentScheduleElement) {
            this.number = paymentScheduleElement.getNumber();
            this.date = paymentScheduleElement.getDate().plusDays(0);
            this.totalPayment = paymentScheduleElement.getTotalPayment().add(new BigDecimal(0));
            this.interestPayment = paymentScheduleElement.getInterestPayment().add(new BigDecimal(0));
            this.debtPayment = paymentScheduleElement.getDebtPayment().add(new BigDecimal(0));
            this.remainingDebt = paymentScheduleElement.getRemainingDebt().add(new BigDecimal(0));
        }
    }
}
