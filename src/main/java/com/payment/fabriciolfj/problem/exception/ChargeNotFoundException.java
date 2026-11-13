package com.payment.fabriciolfj.problem.exception;

import org.springframework.http.HttpStatus;

import static com.payment.fabriciolfj.problem.error.ErrorEnum.CHARGE_NOT_FOUND;

public class ChargeNotFoundException extends BusinessException {

    public ChargeNotFoundException() {
        super(HttpStatus.NOT_FOUND, CHARGE_NOT_FOUND);
    }
}
