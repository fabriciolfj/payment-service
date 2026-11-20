package com.payment.fabriciolfj.adapter.charge;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RequestPaidDTO(String paymentId, BigDecimal value, String endpoint) {
}
