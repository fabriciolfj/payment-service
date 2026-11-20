package com.payment.fabriciolfj.adapter.outbox;

import com.payment.fabriciolfj.repository.outbox.OutboxEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class OutboxProducerEventAdapter {

    public void process(final OutboxEntity entity) {
        log.info("aggregate id {}, send", entity.getAggregateId());
    }
}
