package com.payment.fabriciolfj.repository.idempotency;

import java.util.Optional;

record IdempotencyPersistResult(IdempotencyKeyEntity entity, boolean inserted) {

    Optional<IdempotencyKeyEntity> existing() {
        return inserted ? Optional.empty() : Optional.of(entity);
    }
}
