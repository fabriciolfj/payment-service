package com.payment.fabriciolfj.entrypoint.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ChargeRequest(
        @NotNull(message = "{charge.value.required}")
        BigDecimal value,
        @NotBlank(message = "{charge.describe.required}")
        String describe
) {
}
