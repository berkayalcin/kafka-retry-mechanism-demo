package com.example.kafkaretrymechanismdemo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(final String topic, final String msg) {
        kafkaTemplate.send(topic, msg);
    }

    @SneakyThrows
    public <V> void sendMessage(final String topic, final String key, final V data) {
        final var objectMapper = new ObjectMapper();
        kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(data));
    }
}