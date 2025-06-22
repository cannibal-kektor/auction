BEGIN;

CREATE SEQUENCE IF NOT EXISTS id_sequence_generator
    START WITH 1000
    INCREMENT BY 100
    CACHE 1;

CREATE TABLE IF NOT EXISTS bid
(
    id            BIGINT PRIMARY KEY NOT NULL,
    lot_id        BIGINT             NOT NULL CHECK (lot_id > 0),
    bidder_id     BIGINT             NOT NULL CHECK (bidder_id > 0),
    saga_id       BIGINT             NOT NULL CHECK (saga_id > 0),
    amount        NUMERIC(38, 2)     NOT NULL CHECK (amount > 0),
    creation_time TIMESTAMPTZ        NOT NULL,
    status        VARCHAR(20)        NOT NULL CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'WON')),

    CONSTRAINT uk_saga_id UNIQUE (saga_id)
);

CREATE INDEX IF NOT EXISTS idx_bid_lot ON bid (lot_id);
CREATE INDEX IF NOT EXISTS idx_bid_bidder ON bid (bidder_id);
CREATE INDEX IF NOT EXISTS idx_bid_status ON bid (status);
CREATE INDEX IF NOT EXISTS idx_bid_created ON bid (creation_time);

-- compound index for popular requests
-- CREATE INDEX IF NOT EXISTS idx_bid_status_created ON bid(status, creation_time);
-- aggregation index
-- CREATE INDEX IF NOT EXISTS idx_bid_lot_amount ON bid(lot_id, amount);

DROP TRIGGER IF EXISTS bid_status_trigger ON bid;

CREATE OR REPLACE FUNCTION validate_bid_status()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.status = 'WON' AND NEW.status != OLD.status THEN
        RAISE EXCEPTION 'Cannot modify won bids';
    END IF;

    IF OLD.status = 'REJECTED' AND NEW.status != 'REJECTED' THEN
        RAISE EXCEPTION 'Cannot reactivate rejected bid';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER bid_status_trigger
    BEFORE UPDATE
    ON bid
    FOR EACH ROW
EXECUTE FUNCTION validate_bid_status();

-- Compound indexes for trigger check
CREATE INDEX idx_bid_lot_created ON bid (lot_id, creation_time DESC);
CREATE INDEX idx_bid_lot_amount ON bid (lot_id, amount DESC);

DROP TRIGGER IF EXISTS bid_order_trigger ON bid;

CREATE OR REPLACE FUNCTION validate_bid_order()
    RETURNS TRIGGER AS
$$
DECLARE
    max_amount  NUMERIC(38, 2);
    max_created TIMESTAMPTZ;
BEGIN
    -- Advisory Transaction lock on lot id
    PERFORM pg_advisory_xact_lock(NEW.lot_id);

    SELECT MAX(creation_time)
    INTO max_created
    FROM bid
    WHERE lot_id = NEW.lot_id
      AND status <> 'REJECTED';

    SELECT MAX(amount)
    INTO max_amount
    FROM bid
    WHERE lot_id = NEW.lot_id
      AND status <> 'REJECTED';

    -- Time check
    IF max_created IS NOT NULL AND NEW.creation_time < max_created THEN
        RAISE EXCEPTION 'Bid creation time conflict: latest bid for lot % is %',
            NEW.lot_id, max_created;
    END IF;

--Amount check
    IF max_amount IS NOT NULL AND NEW.amount <= max_amount THEN
        RAISE EXCEPTION 'Bid amount too low: highest bid for lot % is %',
            NEW.lot_id, max_amount;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER bid_order_trigger
    BEFORE INSERT
    ON bid
    FOR EACH ROW
EXECUTE FUNCTION validate_bid_order();

COMMIT;

-- Cacheable table
-- CREATE TABLE lot_max_bid
-- (
--     lot_id        BIGINT PRIMARY KEY REFERENCES lot (id) ON DELETE CASCADE,
--     max_amount    NUMERIC(38, 2) NOT NULL,
--     last_bid_time TIMESTAMPTZ    NOT NULL
-- );
--
-- CREATE OR REPLACE FUNCTION update_max_bid()
--     RETURNS TRIGGER AS
-- $$
-- BEGIN
--     INSERT INTO lot_max_bid (lot_id, max_amount, last_bid_time)
--     VALUES (NEW.lot_id, NEW.amount, NEW.creation_time)
--     ON CONFLICT (lot_id) DO UPDATE
--         SET max_amount    = GREATEST(lot_max_bid.max_amount, EXCLUDED.max_amount),
--             last_bid_time = GREATEST(lot_max_bid.last_bid_time, EXCLUDED.last_bid_time);
--
--     RETURN NEW;
-- END;
-- $$ LANGUAGE PLPGSQL;
--
-- CREATE TRIGGER max_bid_update_trigger
--     AFTER INSERT
--     ON bid
--     FOR EACH ROW
-- EXECUTE FUNCTION update_max_bid();

