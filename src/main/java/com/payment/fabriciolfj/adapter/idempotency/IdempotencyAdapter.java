package com.payment.fabriciolfj.adapter.idempotency;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.problem.exception.ConflitIdempotencyException;
import com.payment.fabriciolfj.repository.idempotency.IdempotencyKeyEntity;
import com.payment.fabriciolfj.repository.idempotency.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.payment.fabriciolfj.adapter.idempotency.IdempotencyKeyEntityMapper.toEntity;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyAdapter {

    private final IdempotencyRepository repository;

    public Optional<ChargeRender> process(final Charge charge, final ChargeRender render) {
        final var idempotency = toEntity(charge, render);

        final var existing = repository.persist(idempotency);

        if (existing.isEmpty()) {
            log.info("persisted idempotency key {}", idempotency);
            return Optional.empty();
        }

        return existing.map(stored -> replay(stored, idempotency));
    }

    private static ChargeRender replay(final IdempotencyKeyEntity stored, final IdempotencyKeyEntity current) {
        if (!stored.getRequestHash().equals(current.getRequestHash())) {
            throw new ConflitIdempotencyException();
        }

        log.info("idempotency replay {}", stored);
        return ChargeRender.builder()
                .body(stored.getResponse())
                .status(stored.getStatusHttp())
                .build();
    }
}
