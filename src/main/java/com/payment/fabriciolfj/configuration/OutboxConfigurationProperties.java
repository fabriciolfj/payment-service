package com.payment.fabriciolfj.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties("outbox")
public class OutboxConfigurationProperties {

    private int batchSize;
    private Attempts attempts;

    @Getter
    @Setter
    public static class Attempts {
        private int max;
    }

    public int getMaxAttempts() {
        return this.attempts.getMax();
    }
}
