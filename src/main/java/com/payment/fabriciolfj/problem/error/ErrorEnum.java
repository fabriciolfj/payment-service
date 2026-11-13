package com.payment.fabriciolfj.problem.error;

import java.util.ResourceBundle;

public enum ErrorEnum {

    MONEY_INVALID,
    CONFLICT_IDEMPOTENCY,
    CHARGE_NOT_FOUND,
    VALIDATION_ERROR;


    public String toMessage() {
        final var bundle = ResourceBundle.getBundle("exceptions/messages");
        return bundle.getString(this.name() + ".message");
    }
}
