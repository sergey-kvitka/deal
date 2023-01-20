package com.kvitka.deal.entities;

import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.jsonEntities.appliedOffer.AppliedOffer;
import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
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
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "applied_offer", nullable = false)
    @Type(type = "AppliedOfferType")
    private AppliedOffer appliedOffer;

    @Column(name = "sign_date", nullable = false)
    private ZonedDateTime signDate;

    // ? TODO ses_code

    @Column(name = "status_history", nullable = false)
    @Type(type = "StatusHistoryType")
    private StatusHistory statusHistory;

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
