package com.example.kafkaretrymechanismdemo.consumer;

import com.example.kafkaretrymechanismdemo.constant.KafkaConstants;
import com.example.kafkaretrymechanismdemo.entity.EmailOutboxItem;
import com.example.kafkaretrymechanismdemo.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailConsumer {

    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = KafkaConstants.SEND_EMAIL_TOPIC, groupId = "send-email-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) final String key,
                       @Payload String event) {
        final var objectMapper = new ObjectMapper();
        try {
            final var emailOutboxItem = objectMapper.readValue(event, EmailOutboxItem.class);
            if (emailOutboxItem.getId() % 2 == 0) {
                throw new Exception("test exception to retry");
            }
            System.out.println(emailOutboxItem.toString());
        } catch (JsonProcessingException e) {
            kafkaProducer.sendMessage(KafkaConstants.SEND_EMAIL_TOPIC_ERROR, key, event);
        } catch (Exception e) {
            kafkaProducer.sendMessage(KafkaConstants.SEND_EMAIL_TOPIC_RETRY, key, event);
        }
    }

    @SneakyThrows
    @KafkaListener(topics = KafkaConstants.SEND_EMAIL_TOPIC_RETRY, groupId = "send-email-consumer-group", containerFactory = "kafkaRetryListenerContainerFactory")
    public void retry(@Payload String event) {
        final var objectMapper = new ObjectMapper();
        final var emailOutboxItem = objectMapper.readValue(event, EmailOutboxItem.class);
        System.out.println(emailOutboxItem.toString());
    }
}
