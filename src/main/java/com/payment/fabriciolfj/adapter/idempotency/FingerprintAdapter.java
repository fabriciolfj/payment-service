package com.payment.fabriciolfj.adapter.idempotency;

import com.payment.fabriciolfj.model.Money;
import lombok.SneakyThrows;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class FingerprintAdapter {

    @SneakyThrows
    public static String of(final Money money, String key) {
        final String canonico = key
                + "|" + money
                .value()
                .setScale(money.getDecimal(), RoundingMode.UNNECESSARY)
                .toPlainString();

        final byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(canonico.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(digest);
    }
}
