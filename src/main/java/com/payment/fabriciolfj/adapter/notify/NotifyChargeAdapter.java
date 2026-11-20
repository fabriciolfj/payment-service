package com.payment.fabriciolfj.adapter.notify;

import com.payment.fabriciolfj.configuration.WebhookConfigurationProperties;
import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.repository.outbox.OutboxRepository;
import com.payment.fabriciolfj.usecase.NotifyChargeGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import static com.payment.fabriciolfj.adapter.charge.RequestPaidDTOMapper.toDTO;
import static com.payment.fabriciolfj.adapter.outbox.OutboxEntityMapper.toEntity;

@Slf4j
@Component
public class NotifyChargeAdapter implements NotifyChargeGateway {

    private final WebhookConfigurationProperties properties;
    private final JsonMapper jsonMapper;
    private final String topic;
    private final OutboxRepository outboxRepository;

    public NotifyChargeAdapter(WebhookConfigurationProperties properties,
                               JsonMapper jsonMapper,
                               @Value("${topic.request-paid}") String topic, OutboxRepository outboxRepository) {
        this.properties = properties;
        this.jsonMapper = jsonMapper;
        this.topic = topic;
        this.outboxRepository = outboxRepository;
    }

    @Override
    public void process(final Charge charge) {
        final var dto = toDTO(charge, properties.getEndpoint());
        log.info("dto mapped {}", dto);

        final var outboxEntity = toEntity(charge,
                jsonMapper.writeValueAsString(dto),
                topic);
        outboxRepository.save(outboxEntity);
    }
}
