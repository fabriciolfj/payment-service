package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.annotation.UseCase;
import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeProcess;
import com.payment.fabriciolfj.model.ChargeRender;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@RequiredArgsConstructor
public class CreateChargeUseCase {

    private final CreateChargeGateway gateway;
    private final ChargeOutputBoundary chargeOutputBoundary;
    private final NotifyChargeGateway notifyChargeGateway;

    @Transactional(propagation = Propagation.REQUIRED)
    public ChargeRender execute(final Charge charge) {
        final var presenter = chargeOutputBoundary.process(charge);
        final var processed = gateway.process(charge, presenter);

        return switch (processed) {
            case ChargeProcess.ChargeCreated created -> {
                notifyChargeGateway.process(charge);
                yield created.render();
            }
            case ChargeProcess.ChargeReplay replay -> replay.render();
        };
    }
}
