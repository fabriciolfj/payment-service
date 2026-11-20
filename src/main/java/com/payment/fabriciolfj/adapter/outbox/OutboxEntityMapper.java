package com.payment.fabriciolfj.adapter.outbox;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.repository.outbox.OutboxEntity;
import com.payment.fabriciolfj.repository.outbox.OutboxStatusEntity;

import java.time.OffsetDateTime;

public class OutboxEntityMapper {

    private OutboxEntityMapper() { }

    public static OutboxEntity toEntity(final Charge charge, final String payload, String topic) {
        return OutboxEntity.builder()
                .aggregateId(charge.getId())
                .createdAt(OffsetDateTime.now())
                .payload(payload)
                .topic(topic)
                .status(OutboxStatusEntity.PENDING)
                .build();
    }
}
