package com.kvitka.deal.jsonEntities.employment;

import com.kvitka.deal.enums.EmploymentPosition;
import com.kvitka.deal.enums.EmploymentStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employment implements Serializable {
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employment that = (Employment) o;
        return status == that.status
                && Objects.equals(employerInn, that.employerInn) && Objects.equals(salary, that.salary)
                && position == that.position && Objects.equals(workExperienceTotal, that.workExperienceTotal)
                && Objects.equals(workExperienceCurrent, that.workExperienceCurrent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, employerInn, salary,
                position, workExperienceTotal, workExperienceCurrent);
    }
}
