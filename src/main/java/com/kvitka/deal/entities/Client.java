package com.kvitka.deal.entities;

import com.kvitka.deal.enums.Gender;
import com.kvitka.deal.enums.MaritalStatus;
import com.kvitka.deal.jsonEntities.employment.Employment;
import com.kvitka.deal.jsonEntities.passport.Passport;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "client")
public class Client {
    @Id
    @Column(name = "client_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name") // * nullable
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthdate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender") // * nullable
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status") // * nullable
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount") // * nullable
    private Integer dependentAmount;

    @Column(name = "passport", nullable = false,
            columnDefinition = "jsonb")
    @Type(type = "PassportType")
    private Passport passport;

    @Column(name = "employment",
            columnDefinition = "jsonb") // * nullable
    @Type(type = "EmploymentType")
    private Employment employment;

    @Column(name = "account") // * nullable
    private String account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Client client = (Client) o;
        return clientId != null && Objects.equals(clientId, client.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, firstName, lastName, middleName, birthdate, email,
                gender, maritalStatus, dependentAmount, passport, employment, account);
    }
}
