CREATE TABLE charge (
    id           UUID          PRIMARY KEY,
    amount       NUMERIC(12,2) NOT NULL,
    description  VARCHAR(100)  NOT NULL,
    status       VARCHAR(20)   NOT NULL,
    created_at   TIMESTAMPTZ   NOT NULL,
    expires_at   TIMESTAMPTZ   NOT NULL,
    paid_at      TIMESTAMPTZ,
    version      BIGINT        NOT NULL DEFAULT 0,

    CONSTRAINT ck_charge_amount_positive CHECK (amount > 0),
    CONSTRAINT ck_charge_description_not_blank CHECK (btrim(description) <> ''),
    CONSTRAINT ck_charge_status CHECK (status IN ('PENDING', 'PAID', 'CANCELED', 'EXPIRED')),
    CONSTRAINT ck_charge_expires_after_created CHECK (expires_at > created_at),
    CONSTRAINT ck_charge_paid_at_when_paid CHECK ((status = 'PAID') = (paid_at IS NOT NULL))
);

CREATE INDEX ix_charge_pending_expires_at ON charge (expires_at) WHERE status = 'PENDING';

CREATE INDEX ix_charge_status_created_at ON charge (status, created_at DESC);