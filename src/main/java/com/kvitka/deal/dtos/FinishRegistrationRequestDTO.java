package com.kvitka.deal.dtos;

import com.kvitka.deal.entities.Employment;
import com.kvitka.deal.enums.Gender;
import com.kvitka.deal.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinishRegistrationRequestDTO {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private Employment employment;
    private String account;
}
