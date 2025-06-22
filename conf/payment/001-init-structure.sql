BEGIN;

CREATE SEQUENCE IF NOT EXISTS ID_SEQUENCE_GENERATOR
    INCREMENT BY 100
    START WITH 2000
    CACHE 1;

CREATE TABLE IF NOT EXISTS payment_accounts
(
    id      BIGINT PRIMARY KEY,
    balance NUMERIC(19, 4) NOT NULL DEFAULT 0.0000 CHECK (balance >= 0),
    version BIGINT         NOT NULL CHECK (version >= 0),
    user_id BIGINT         NOT NULL CHECK (user_id > 0),

    CONSTRAINT uq_user_account UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS balance_operations
(
    id             BIGINT PRIMARY KEY,
    amount         NUMERIC(19, 4) NOT NULL CHECK (amount > 0),
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    account_id     BIGINT         NOT NULL REFERENCES payment_accounts (id) ON DELETE CASCADE,
    operation_type VARCHAR(20)    NOT NULL CHECK (operation_type IN ('CREDIT', 'DEBIT')),

    CONSTRAINT fk_operation_account FOREIGN KEY (account_id) REFERENCES payment_accounts (id)
);

CREATE TABLE IF NOT EXISTS credit_operations
(
    operation_id BIGINT PRIMARY KEY REFERENCES balance_operations (id) ON DELETE CASCADE,
    status       VARCHAR(20) NOT NULL CHECK (status IN ('RESERVED', 'ACCEPTED', 'CANCELLED')),
    bid_id       BIGINT CHECK (bid_id > 0),
    saga_id      BIGINT      NOT NULL CHECK (saga_id > 0),

    CONSTRAINT uk_saga_id UNIQUE (saga_id)
);

CREATE TABLE IF NOT EXISTS debit_operations
(
    operation_id BIGINT PRIMARY KEY REFERENCES balance_operations (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_operations_account ON balance_operations (account_id);
CREATE INDEX IF NOT EXISTS idx_operations_created ON balance_operations (created_at);
CREATE INDEX IF NOT EXISTS idx_credit_status ON credit_operations (status);
CREATE INDEX IF NOT EXISTS idx_credit_bid ON credit_operations (bid_id);
CREATE INDEX IF NOT EXISTS idx_account_user ON payment_accounts (user_id);

DROP TRIGGER IF EXISTS check_balance ON balance_operations;

CREATE OR REPLACE FUNCTION check_balance_update()
    RETURNS TRIGGER AS
$$
DECLARE
    current_balance NUMERIC(19, 4);
BEGIN
    IF TG_OP = 'INSERT' AND NEW.operation_type = 'CREDIT' THEN

        SELECT balance
        INTO current_balance
        FROM payment_accounts
        WHERE id = NEW.account_id
            FOR UPDATE;

        IF current_balance < NEW.amount THEN
            RAISE EXCEPTION 'Insufficient funds: BALANCE=%, attempted CREDIT=%',
                current_balance, NEW.amount;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_balance
    BEFORE INSERT
    ON balance_operations
    FOR EACH ROW
EXECUTE FUNCTION check_balance_update();

COMMIT;

