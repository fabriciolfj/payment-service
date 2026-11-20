CREATE TABLE outbox (
    id              BIGINT        GENERATED ALWAYS AS IDENTITY,
    aggregate_id    UUID          NOT NULL,
    payload         JSONB         NOT NULL,
    topic           VARCHAR(100)  NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    attempts        INT           NOT NULL DEFAULT 0,
    locked_at       TIMESTAMPTZ,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT  pk_outbox PRIMARY KEY (id),
    CONSTRAINT  ck_outbox_status CHECK (status IN ('PENDING', 'PROCESSING', 'SENT', 'FAILED'))
);

CREATE INDEX idx_outbox_pending ON outbox (created_at) WHERE status = 'PENDING';
CREATE INDEX idx_outbox_processing ON outbox (locked_at) WHERE status = 'PROCESSING';
