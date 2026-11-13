package com.payment.fabriciolfj.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ChargeRender(UUID key, String body, int status) {
}
