package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.model.Charge;

import java.util.Optional;
import java.util.UUID;

public interface FindChargeGateway {

    Optional<Charge> process(final UUID id);
}
