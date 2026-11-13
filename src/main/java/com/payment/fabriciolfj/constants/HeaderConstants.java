package com.payment.fabriciolfj.constants;

public class HeaderConstants {

    private HeaderConstants() {

    }

    public static final String IDEMPOTENCE_KEY = "x-idempotence-key";
    public static final String CORRELATION_ID = "x-correlation_id";

    public static final int IDEMPOTENCE_KEY_MIN_SIZE = 16;
    public static final int IDEMPOTENCE_KEY_MAX_SIZE = 64;
}
