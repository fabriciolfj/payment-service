package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.annotation.UseCase;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.problem.exception.ChargeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindChargeUseCase {

    private final FindChargeGateway gateway;
    private final FindChargeOutputBoundary findChargeOutputBoundary;

    @Transactional(readOnly = true)
    public ChargeRender execute(final UUID id) {
        return gateway.process(id)
                .map(findChargeOutputBoundary::process)
                .orElseThrow(ChargeNotFoundException::new);
    }
}
