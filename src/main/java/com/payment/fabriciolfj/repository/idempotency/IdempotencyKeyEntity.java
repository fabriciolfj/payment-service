package com.payment.fabriciolfj.repository.idempotency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "idempotency_key")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(exclude = "response")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IdempotencyKeyEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "key", nullable = false, updatable = false, length = 100)
    private String key;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Column(name = "status_http")
    private Integer statusHttp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response", columnDefinition = "jsonb")
    private String response;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    @PrePersist
    void prePersist() {
        if (createdOn == null) {
            createdOn = Instant.now();
        }
    }
}
