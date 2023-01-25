package com.kvitka.deal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport implements Serializable {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

    public Passport(String series, String number) {
        this.series = series;
        this.number = number;
        this.issueBranch = null;
        this.issueDate = null;
    }
}
