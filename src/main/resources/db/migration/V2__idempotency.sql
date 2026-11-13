CREATE TABLE idempotency_key (
    key            VARCHAR(100) PRIMARY KEY,
    request_hash   VARCHAR(64)  NOT NULL,
    status_http    INT,
    response       JSONB,
    created_on     TIMESTAMPTZ  NOT NULL DEFAULT now()
);