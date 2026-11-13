package com.payment.fabriciolfj.model;

import com.payment.fabriciolfj.problem.exception.MoneyInvalidException;

import java.math.BigDecimal;

public record Money(BigDecimal value, Currency currency) {

    public Money {
        if (isValueNegative(value) || isValueDecimalInvalid(value, currency)) {
            throw new MoneyInvalidException();
        }
    }

    public int getDecimal() {
        return currency.getDecimal();
    }

    public static Money of(BigDecimal value) {
        return new Money(value, Currency.BR);
    }

    private static boolean isValueNegative(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

    private static boolean isValueDecimalInvalid(BigDecimal value, Currency currency) {
        var valueDecimal = value.setScale(currency.getDecimal());

        return value.compareTo(valueDecimal) != 0;
    }
}
