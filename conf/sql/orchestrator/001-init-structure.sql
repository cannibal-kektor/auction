BEGIN;

CREATE SEQUENCE IF NOT EXISTS id_sequence_generator
    START WITH 1000
    INCREMENT BY 100
    CACHE 1;

CREATE TABLE IF NOT EXISTS saga
(
    saga_id                   BIGINT PRIMARY KEY,
    status                    VARCHAR(20)    NOT NULL,
    lot_id                    BIGINT         NOT NULL,
    creation_time             TIMESTAMPTZ    NOT NULL,
    bidder_id                 BIGINT         NOT NULL,
    payment_account_id        BIGINT         NOT NULL,
    new_bid_amount            NUMERIC(19, 4) NOT NULL,
    compensate_bid_amount     NUMERIC(19, 4) NOT NULL,
    compensate_winning_bid_id BIGINT         NOT NULL,
    compensate_winner_id      BIGINT         NOT NULL,
    problem_detail            JSON,

    CONSTRAINT valid_status CHECK (status IN ('ACTIVE', 'STALLED', 'COMPLETED', 'COMPENSATED')),
    CONSTRAINT positive_lot_id CHECK (lot_id > 0),
    CONSTRAINT positive_bidder_id CHECK (bidder_id > 0),
    CONSTRAINT positive_payment_account_id CHECK (payment_account_id > 0),
    CONSTRAINT positive_winning_bid CHECK (compensate_winning_bid_id > 0),
    CONSTRAINT positive_winner_id CHECK (compensate_winner_id > 0),
    CONSTRAINT positive_new_bid CHECK (new_bid_amount > 0),
    CONSTRAINT positive_compensate_bid CHECK (compensate_bid_amount > 0)
);

-- Индексы
-- CREATE INDEX idx_saga_status ON saga (status);
-- CREATE INDEX idx_saga_created ON saga (creation_time);
CREATE UNIQUE INDEX IF NOT EXISTS only_one_active_saga_per_lot ON saga (lot_id)
    WHERE status IN ('ACTIVE', 'STALLED');

DROP TRIGGER IF EXISTS trg_prevent_immutable_update ON saga;

-- Immutable
CREATE OR REPLACE FUNCTION prevent_immutable_update()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.lot_id <> NEW.lot_id THEN
        RAISE EXCEPTION 'lot_id cannot be updated';
    END IF;
    IF OLD.creation_time <> NEW.creation_time THEN
        RAISE EXCEPTION 'creation_time cannot be updated';
    END IF;
    IF OLD.bidder_id <> NEW.bidder_id THEN
        RAISE EXCEPTION 'bidder_id cannot be updated';
    END IF;
    IF OLD.new_bid_amount <> NEW.new_bid_amount THEN
        RAISE EXCEPTION 'new_bid_amount cannot be updated';
    END IF;
    IF OLD.compensate_bid_amount <> NEW.compensate_bid_amount THEN
        RAISE EXCEPTION 'compensate_bid_amount cannot be updated';
    END IF;
    IF OLD.compensate_winning_bid_id <> NEW.compensate_winning_bid_id THEN
        RAISE EXCEPTION 'compensate_winning_bid_id cannot be updated';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_immutable_update
    BEFORE UPDATE
    ON saga
    FOR EACH ROW
EXECUTE FUNCTION prevent_immutable_update();

COMMIT;

