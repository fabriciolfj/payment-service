package com.payment.fabriciolfj.entrypoint.controller;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.Money;
import com.payment.fabriciolfj.model.Status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ChargeMapper {

    private ChargeMapper() {

    }

    public static Charge of(final ChargeRequest request) {
        final var money = Money.of(request.value());

        return Charge.builder()
                .id(UUID.randomUUID())
                .createdOn(Instant.now())
                .money(money)
                .expired(Instant.now().plus(30, ChronoUnit.MINUTES))
                .describe(request.describe())
                .status(Status.PENDING)
                .build();
    }
}
