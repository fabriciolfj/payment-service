package com.payment.fabriciolfj.configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Setter
@Getter
@Configuration
@ConfigurationProperties("webhook")
public class WebhookConfigurationProperties {

    @NotEmpty
    private String endpoint;
}
