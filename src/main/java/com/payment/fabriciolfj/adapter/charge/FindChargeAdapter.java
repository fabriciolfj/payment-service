package com.payment.fabriciolfj.adapter.charge;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.repository.charge.ChargeRepository;
import com.payment.fabriciolfj.usecase.FindChargeGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindChargeAdapter implements FindChargeGateway {

    private final ChargeRepository repository;

    @Override
    public Optional<Charge> process(final UUID id) {
        return repository.findById(id)
                .map(ChargeEntityMapper::toModel);
    }
}
