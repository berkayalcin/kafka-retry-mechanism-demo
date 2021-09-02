package com.example.kafkaretrymechanismdemo.constant;

public final class KafkaConstants {
    public static final String SEND_EMAIL_TOPIC = "send.email";
    public static final String SEND_EMAIL_TOPIC_RETRY = "send.email.sample_consumer_group.RETRY";
    public static final String SEND_EMAIL_TOPIC_ERROR = "send.email.sample_consumer_group.ERROR";
    public static final int MAX_RETRY_COUNT = 5;
}
