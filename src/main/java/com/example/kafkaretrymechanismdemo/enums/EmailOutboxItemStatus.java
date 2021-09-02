package com.example.kafkaretrymechanismdemo.enums;

public enum EmailOutboxItemStatus {
    CREATED,
    DELIVERED,
    HAS_NO_RETRY_ERROR,
    HAS_RETRYABLE_ERROR
}
