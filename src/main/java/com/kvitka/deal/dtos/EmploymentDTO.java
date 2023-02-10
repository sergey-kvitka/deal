package com.kvitka.deal.dtos;

import com.kvitka.deal.enums.EmploymentPosition;
import com.kvitka.deal.enums.EmploymentStatus;
import com.kvitka.deal.jsonEntities.employment.Employment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
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
        log.info("Converting EmploymentDTO to Employment (EmploymentDTO = {})", this);
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
        log.info("Creating EmploymentDTO by converting it from Employment (Employment = {})", employment);
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
