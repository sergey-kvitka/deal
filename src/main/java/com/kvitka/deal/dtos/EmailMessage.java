package com.kvitka.deal.dtos;

import com.kvitka.deal.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    String address;
    Theme theme;
    Long applicationId;
    String info;

    public EmailMessage(String address, Theme theme, Long applicationId) {
        this.address = address;
        this.theme = theme;
        this.applicationId = applicationId;
    }
}
