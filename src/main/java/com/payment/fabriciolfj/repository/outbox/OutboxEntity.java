package com.payment.fabriciolfj.repository.outbox;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
@Table(name = "outbox")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    @Column(name = "aggregate_id", columnDefinition = "UUID", nullable = false)
    private UUID aggregateId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false)
    private String payload;
    @Column(name = "topic", length = 100, nullable = false)
    private String topic;
    @Column(name = "attempts", nullable = false)
    private int attempts;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutboxStatusEntity status;
    @Column(name = "locked_at")
    private OffsetDateTime lockedAt;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public OutboxEntity updateAttempts() {
        this.attempts = this.attempts + 1;
        this.status = OutboxStatusEntity.PENDING;
        return this;
    }

    public OutboxEntity updateStatusFailed() {
        this.status = OutboxStatusEntity.FAILED;
        return this;
    }

    public OutboxEntity updateStatusSent() {
        this.status = OutboxStatusEntity.SENT;
        return this;
    }
}
