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

        final var result = repository.persist(idempotency);

        if (result.isEmpty()) {
            log.info("inserted idempotency key {}", idempotency.getKey());
            return Optional.empty();
        }

        return result.map(v -> replay(v, idempotency));
    }

    private ChargeRender replay(final IdempotencyKeyEntity entity, final IdempotencyKeyEntity currency) {
        if (entity.getRequestHash().equals(currency.getRequestHash())) {
            return ChargeRender.builder()
                    .status(entity.getStatusHttp())
                    .body(entity.getResponse())
                    .build();
        }

        throw new ConflitIdempotencyException();
    }

}
