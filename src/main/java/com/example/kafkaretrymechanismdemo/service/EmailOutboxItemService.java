package com.example.kafkaretrymechanismdemo.service;

import com.example.kafkaretrymechanismdemo.constant.KafkaConstants;
import com.example.kafkaretrymechanismdemo.entity.EmailOutboxItem;
import com.example.kafkaretrymechanismdemo.enums.EmailOutboxItemStatus;
import com.example.kafkaretrymechanismdemo.producer.KafkaProducer;
import com.example.kafkaretrymechanismdemo.repository.EmailOutboxItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailOutboxItemService {
    private final EmailOutboxItemRepository emailOutboxItemRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void process() {
        final var optionalEmailOutboxItem = emailOutboxItemRepository.findFirstBySendDateIsLessThanEqualAndStatusInOrderByOrderDesc(
                DateTime.now().toDate(),
                List.of(EmailOutboxItemStatus.CREATED, EmailOutboxItemStatus.HAS_RETRYABLE_ERROR)
        );

        if (optionalEmailOutboxItem.isEmpty())
            return;

        final var emailOutboxItem = optionalEmailOutboxItem.get();
        try {
            kafkaProducer.sendMessage(KafkaConstants.SEND_EMAIL_TOPIC, Long.toString(emailOutboxItem.getId()), emailOutboxItem);
            emailOutboxItem.setStatus(EmailOutboxItemStatus.DELIVERED);
            emailOutboxItemRepository.save(emailOutboxItem);
        } catch (Exception e) {
            emailOutboxItem.setStatus(EmailOutboxItemStatus.HAS_RETRYABLE_ERROR);
            emailOutboxItem.setErrorMessage(e.toString());
            emailOutboxItemRepository.save(emailOutboxItem);
        }


    }
}
