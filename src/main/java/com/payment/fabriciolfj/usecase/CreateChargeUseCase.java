package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.annotation.UseCase;
import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@RequiredArgsConstructor
public class CreateChargeUseCase {

    private final CreateChargeGateway gateway;
    private final ChargeOutputBoundary chargeOutputBoundary;

    @Transactional(propagation = Propagation.REQUIRED)
    public ChargeRender execute(final Charge charge) {
        final var presenter = chargeOutputBoundary.process(charge);

        return gateway.process(charge, presenter);
    }
}
