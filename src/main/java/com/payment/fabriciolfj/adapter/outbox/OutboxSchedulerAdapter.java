package com.payment.fabriciolfj.adapter.outbox;

import com.payment.fabriciolfj.configuration.OutboxConfigurationProperties;
import com.payment.fabriciolfj.repository.outbox.OutboxRepository;
import org.springframework.stereotype.Component;

@Component
public class OutboxSchedulerAdapter {

    private final OutboxConfigurationProperties properties;
    private final OutboxRepository repository;
    private final OutboxProcessBatchAdapter processBatchAdapter;

    public OutboxSchedulerAdapter(final OutboxConfigurationProperties properties,
                                  final OutboxRepository repository,
                                  final OutboxProcessBatchAdapter processBatchAdapter) {
        this.properties = properties;
        this.repository = repository;
        this.processBatchAdapter = processBatchAdapter;
    }

    public void process() {
        repository.claimBatch(properties.getBatchSize())
                .stream()
                .map(entity -> processBatchAdapter.execute(entity, properties.getMaxAttempts()))
                .forEach(repository::save);
    }
}
