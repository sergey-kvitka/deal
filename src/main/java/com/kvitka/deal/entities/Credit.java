package com.kvitka.deal.entities;

import com.kvitka.deal.enums.CreditStatus;
import com.kvitka.deal.jsonEntities.paymentSchedule.PaymentSchedule;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "credit")
public class Credit {
    @Id
    @Column(name = "credit_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "term", nullable = false)
    private Integer term;

    @Column(name = "monthly_payment", nullable = false)
    private BigDecimal monthlyPayment;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "psk", nullable = false)
    private BigDecimal psk;

    @Column(name = "payment_schedule", nullable = false,
            columnDefinition = "jsonb")
    @Type(type = "PaymentScheduleType")
    private PaymentSchedule paymentSchedule;

    @Column(name = "insurance_enable", nullable = false)
    private Boolean insuranceEnable;

    @Column(name = "salary_client", nullable = false)
    private Boolean salaryClient;

    @Column(name = "credit_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Credit credit = (Credit) o;
        return creditId != null && Objects.equals(creditId, credit.creditId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditId, amount, term, monthlyPayment, rate, psk,
                paymentSchedule, insuranceEnable, salaryClient, creditStatus);
    }
}
