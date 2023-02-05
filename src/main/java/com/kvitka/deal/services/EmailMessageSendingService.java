package com.kvitka.deal.services;

import com.kvitka.deal.dtos.EmailMessage;

public interface EmailMessageSendingService {
    void sendMessage(EmailMessage message);
}
