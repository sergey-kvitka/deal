package com.kvitka.deal.services.impl;

import com.kvitka.deal.dtos.EmailMessage;
import com.kvitka.deal.services.EmailMessageSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSendingServiceImpl implements EmailMessageSendingService {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @Override
    public void sendMessage(EmailMessage message) {
        String topic = message.getTheme().topic();
        log.info("Message is about to be sent to topic '{}' (message: {})", topic, message);
        kafkaTemplate.send(topic, message);
        log.info("Message sent successfully! Topic: '{}', message: {}", topic, message);
    }

}
