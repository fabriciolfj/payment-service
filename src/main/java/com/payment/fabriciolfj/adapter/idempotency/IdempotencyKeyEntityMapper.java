package com.payment.fabriciolfj.adapter.idempotency;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.repository.idempotency.IdempotencyKeyEntity;
import org.slf4j.MDC;

import static com.payment.fabriciolfj.constants.MdcConstants.IDEMPOTENCY_KEY;

public class IdempotencyKeyEntityMapper {

    private IdempotencyKeyEntityMapper() { }

    public static IdempotencyKeyEntity toEntity(final Charge charge, final ChargeRender render) {
        final var key =  MDC.get(IDEMPOTENCY_KEY);
        final var fingerprint = FingerprintAdapter.of(charge.getMoney(), key);

        return IdempotencyKeyEntity.builder()
                .key(key)
                .requestHash(fingerprint)
                .statusHttp(render.status())
                .response(render.body())
                .build();
    }
}
