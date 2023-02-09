package com.kvitka.deal.dtos;

import com.kvitka.deal.enums.Theme;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "info")
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
