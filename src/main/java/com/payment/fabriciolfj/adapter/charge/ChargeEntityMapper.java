package com.payment.fabriciolfj.adapter.charge;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.Money;
import com.payment.fabriciolfj.repository.charge.ChargeEntity;

public class ChargeEntityMapper {

    private ChargeEntityMapper() { }

    public static ChargeEntity toEntity(final Charge charge) {
        return ChargeEntity.builder()
                .id(charge.getId())
                .amount(charge.getMoney().value())
                .description(charge.getDescribe())
                .status(charge.getStatus())
                .createdAt(charge.getCreatedOn())
                .expiresAt(charge.getExpired())
                .build();
    }

    public static Charge toModel(final ChargeEntity entity) {
        return Charge.builder()
                .id(entity.getId())
                .money(Money.of(entity.getAmount()))
                .describe(entity.getDescription())
                .status(entity.getStatus())
                .createdOn(entity.getCreatedAt())
                .expired(entity.getExpiresAt())
                .build();
    }
}
