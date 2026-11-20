package com.payment.fabriciolfj.adapter.outbox;

import com.payment.fabriciolfj.repository.outbox.OutboxEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxProcessBatchAdapter {

    private final OutboxProducerEventAdapter producerEventAdapter;

    public OutboxEntity execute(final OutboxEntity outboxEntity, final int maxAttempts) {
        try {
            producerEventAdapter.process(outboxEntity);

            return outboxEntity.updateStatusSent();
        } catch (Exception e) {
            log.error("fail send outbox, cause {}, aggregateid {}",
                    e.getMessage(),
                    outboxEntity.getAggregateId());

            return handlerProblemaOutbox(outboxEntity, maxAttempts);
        }
    }

    private OutboxEntity handlerProblemaOutbox(OutboxEntity outboxEntity, int maxAttempts) {
        final var outboxUpdated = outboxEntity.updateAttempts();

        if (outboxUpdated.getAttempts() >= maxAttempts) {
            return outboxEntity.updateStatusFailed();
        }

        return outboxUpdated;
    }
}
