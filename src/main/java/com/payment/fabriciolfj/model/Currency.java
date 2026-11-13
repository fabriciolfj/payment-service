package com.payment.fabriciolfj.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    BR(2);

    private final int decimal;
}
