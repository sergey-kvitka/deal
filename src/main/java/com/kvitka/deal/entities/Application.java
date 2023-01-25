package com.kvitka.deal.entities;


import com.kvitka.deal.enums.ApplicationStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "application")
public class Application {
    @Id
    @Column(name = "application_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long applicationId;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit_id") // * nullable
    private Credit credit;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "applied_offer") // * nullable
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOffer appliedOffer;

    @Column(name = "sign_date") // * nullable
    private ZonedDateTime signDate;

    @Column(name = "ses_code") // * nullable
    private Integer sesCode;

    @Column(name = "status_history", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private StatusHistoryList statusHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Application that = (Application) o;
        return applicationId != null && Objects.equals(applicationId, that.applicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, client, credit, status,
                creationDate, appliedOffer, signDate, statusHistory);
    }
}
