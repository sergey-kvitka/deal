package com.kvitka.deal.dtos;

import com.kvitka.deal.enums.EmploymentPosition;
import com.kvitka.deal.enums.EmploymentStatus;
import com.kvitka.deal.jsonEntities.employment.Employment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

    public Employment toEmployment() {
        return new Employment(
                employmentStatus,
                employerINN,
                salary,
                position,
                workExperienceTotal,
                workExperienceCurrent
        );
    }

    public static EmploymentDTO from(Employment employment) {
        return new EmploymentDTO(
                employment.getStatus(),
                employment.getEmployerInn(),
                employment.getSalary(),
                employment.getPosition(),
                employment.getWorkExperienceTotal(),
                employment.getWorkExperienceCurrent()
        );
    }
}
