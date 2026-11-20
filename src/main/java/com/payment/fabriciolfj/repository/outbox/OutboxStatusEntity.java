package com.payment.fabriciolfj.repository.outbox;

public enum OutboxStatusEntity {

    PENDING,
    PROCESSING,
    SENT,
    FAILED
}
