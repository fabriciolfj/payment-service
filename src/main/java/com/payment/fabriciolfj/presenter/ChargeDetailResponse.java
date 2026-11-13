package com.payment.fabriciolfj.presenter;

import java.math.BigDecimal;
import java.time.Instant;

public record ChargeDetailResponse(String id,
                                   BigDecimal value,
                                   String describe,
                                   String status,
                                   Instant createdOn,
                                   Instant expiresAt) {
}
