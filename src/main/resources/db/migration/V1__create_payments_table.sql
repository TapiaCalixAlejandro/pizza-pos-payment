CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'MXN',
    channel VARCHAR(20) NOT NULL,
    method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    transaction_reference VARCHAR(100),
    idempotency_key VARCHAR(100) NOT NULL,
    amount_received NUMERIC(10, 2),
    change_given NUMERIC(10, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- El UNIQUE aquí es VITAL para evitar cobros dobles (Race conditions)
    CONSTRAINT uk_payment_idempotency_key UNIQUE (idempotency_key)
);

CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);