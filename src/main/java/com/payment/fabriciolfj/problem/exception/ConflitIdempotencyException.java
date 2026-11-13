package com.payment.fabriciolfj.problem.exception;

import org.springframework.http.HttpStatus;

import static com.payment.fabriciolfj.problem.error.ErrorEnum.CONFLICT_IDEMPOTENCY;

public class ConflitIdempotencyException extends BusinessException {

    public ConflitIdempotencyException() {
        super(HttpStatus.CONFLICT, CONFLICT_IDEMPOTENCY);
    }
}
