BEGIN;

CREATE SEQUENCE IF NOT EXISTS ID_SEQUENCE_GENERATOR
    INCREMENT BY 100
    START WITH 2000
    CACHE 1;

CREATE TABLE IF NOT EXISTS payment_accounts
(
    id         BIGINT PRIMARY KEY,
    balance    NUMERIC(19, 4) NOT NULL DEFAULT 0.0000 CHECK (balance >= 0),
    version    BIGINT         NOT NULL CHECK (version >= 0),
    user_id    BIGINT         NOT NULL CHECK (user_id > 0),
    registration_date TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_user_account UNIQUE (user_id)
);

CREATE INDEX IF NOT EXISTS idx_account_user ON payment_accounts (user_id);

CREATE TABLE IF NOT EXISTS balance_operations
(
    id             BIGINT PRIMARY KEY,
    amount         NUMERIC(19, 4) NOT NULL CHECK (amount > 0),
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    account_id     BIGINT         NOT NULL REFERENCES payment_accounts (id) ON DELETE CASCADE,
    operation_type VARCHAR(20)    NOT NULL CHECK (operation_type IN ('CREDIT', 'DEBIT')),
    -- CreditOperation
    credit_status  VARCHAR(20),
    bid_id         BIGINT,
    saga_id        BIGINT,
    CONSTRAINT fk_operation_account FOREIGN KEY (account_id) REFERENCES payment_accounts (id),

    -- Checks for CREDIT
    CONSTRAINT credit_status_check CHECK (
        (operation_type = 'CREDIT' AND credit_status IN ('RESERVED', 'ACCEPTED', 'CANCELLED'))
            OR (operation_type = 'DEBIT' AND credit_status IS NULL)
        ),

    CONSTRAINT bid_id_check CHECK (
        (operation_type = 'CREDIT' AND bid_id > 0)
            OR (operation_type = 'DEBIT' AND bid_id IS NULL)
        ),

    CONSTRAINT saga_id_check CHECK (
        (operation_type = 'CREDIT' AND saga_id IS NOT NULL AND saga_id > 0)
            OR (operation_type = 'DEBIT' AND saga_id IS NULL)
        )
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_saga_id ON balance_operations (saga_id)
    WHERE operation_type = 'CREDIT';
CREATE INDEX IF NOT EXISTS idx_operations_account ON balance_operations (account_id);

CREATE INDEX IF NOT EXISTS idx_operations_created ON balance_operations (created_at);

CREATE INDEX IF NOT EXISTS idx_credit_status ON balance_operations (credit_status)
    WHERE operation_type = 'CREDIT';

CREATE INDEX IF NOT EXISTS idx_credit_bid ON balance_operations (bid_id)
    WHERE operation_type = 'CREDIT' AND bid_id IS NOT NULL;

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

