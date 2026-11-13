package com.payment.fabriciolfj.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Charge {

    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID id;
    private Money money;
    @ToString.Include
    private String describe;
    private Status status;
    @ToString.Include
    private Instant createdOn;
    @ToString.Include
    private Instant expired;

}
