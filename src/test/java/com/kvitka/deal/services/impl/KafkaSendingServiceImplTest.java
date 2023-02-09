package com.kvitka.deal.services.impl;

import com.kvitka.deal.dtos.EmailMessage;
import com.kvitka.deal.enums.Theme;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class KafkaSendingServiceImplTest {

    @Mock
    private KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @InjectMocks
    private KafkaSendingServiceImpl kafkaSendingService;

    @Test
    @Order(1)
    @DisplayName("KafkaSendingService.sendMessage method test")
    void sendMessage() {
        EmailMessage emailMessage = new EmailMessage("email...", Theme.APPLICATION_DENIED, -1L);
        kafkaSendingService.sendMessage(emailMessage);

        verify(kafkaTemplate, times(1)).send(any(), any());
    }
}