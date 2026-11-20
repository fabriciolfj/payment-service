package com.payment.fabriciolfj.adapter.charge;

import com.payment.fabriciolfj.adapter.idempotency.IdempotencyAdapter;
import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.model.ChargeProcess;
import com.payment.fabriciolfj.repository.charge.ChargeRepository;
import com.payment.fabriciolfj.usecase.CreateChargeGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.payment.fabriciolfj.adapter.charge.ChargeEntityMapper.toEntity;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateChargeAdapter implements CreateChargeGateway {

    private final IdempotencyAdapter idempotencyAdapter;
    private final ChargeRepository repository;

    @Override
    public ChargeProcess process(final Charge charge, final ChargeRender render) {
        final var result = idempotencyAdapter.process(charge, render);
        if (result.isEmpty()) {
            save(charge);

            return new ChargeProcess.ChargeCreated(render);
        }

        return new ChargeProcess.ChargeReplay(result.get());
    }

    private void save(final Charge charge) {
        repository.save(toEntity(charge));
        log.info("charge save success {}", charge.getId());
    }
}
