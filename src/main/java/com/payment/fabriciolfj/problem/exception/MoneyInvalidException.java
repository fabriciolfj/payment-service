package com.payment.fabriciolfj.problem.exception;

import org.springframework.http.HttpStatus;

import static com.payment.fabriciolfj.problem.error.ErrorEnum.MONEY_INVALID;

public class MoneyInvalidException extends BusinessException {

    public MoneyInvalidException() {
        super(HttpStatus.UNPROCESSABLE_CONTENT, MONEY_INVALID);
    }
}
