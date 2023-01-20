package com.kvitka.deal.jsonEntities.passport;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Passport implements Serializable {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passport passport = (Passport) o;
        return Objects.equals(series, passport.series) && Objects.equals(number, passport.number)
                && Objects.equals(issueBranch, passport.issueBranch) && Objects.equals(issueDate, passport.issueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(series, number, issueBranch, issueDate);
    }
}
