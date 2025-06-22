BEGIN;

CREATE SEQUENCE IF NOT EXISTS id_sequence_generator
    START WITH 1000
    INCREMENT BY 100
    CACHE 1;

CREATE TABLE IF NOT EXISTS lot
(
    id             BIGINT PRIMARY KEY NOT NULL,
    version        BIGINT             NOT NULL,
    name           VARCHAR(255)       NOT NULL,
    status         VARCHAR(20)        NOT NULL,
    description    VARCHAR(4000)      NOT NULL,
    seller_id      BIGINT             NOT NULL,
    initial_price  NUMERIC(38, 2)     NOT NULL CHECK (initial_price > 0),
    auction_end    TIMESTAMPTZ        NOT NULL,
    auction_start  TIMESTAMPTZ        NOT NULL,
    highest_bid    NUMERIC(38, 2)     NOT NULL DEFAULT 0,
    bids_count     BIGINT             NOT NULL DEFAULT 0,
    winning_bid_id BIGINT             NOT NULL DEFAULT 0,

    CONSTRAINT valid_status CHECK (status IN ('PENDING', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT auction_time_check CHECK (auction_start < auction_end),
    CONSTRAINT name_length_check CHECK (LENGTH(name) >= 2),
    CONSTRAINT description_length_check CHECK (LENGTH(description) >= 10),
    CONSTRAINT auction_lot_duration_check CHECK (auction_end - auction_start > INTERVAL '1 hour')
);

CREATE TABLE IF NOT EXISTS lot_categories
(
    category_id BIGINT NOT NULL CHECK (category_id > 0),
    lot_id      BIGINT NOT NULL,
    PRIMARY KEY (lot_id, category_id)
);

ALTER TABLE IF EXISTS lot_categories
    ADD CONSTRAINT lot_categories_foreign_key
        FOREIGN KEY (lot_id) REFERENCES lot
            ON DELETE CASCADE;


CREATE INDEX IF NOT EXISTS idx_lot_category ON lot_categories (category_id);
CREATE INDEX IF NOT EXISTS idx_lot_seller ON lot (seller_id);
CREATE INDEX IF NOT EXISTS idx_lots_status_start ON lot (status, auction_start);
CREATE INDEX IF NOT EXISTS idx_lots_status_end ON lot (status, auction_end);

DROP TRIGGER IF EXISTS lot_auction_time_trigger ON lot;

CREATE OR REPLACE FUNCTION validate_auction_time()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.auction_start < NOW() OR NEW.auction_end < NOW() THEN
        RAISE EXCEPTION 'Auction times must be in the future';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER lot_auction_time_trigger
    BEFORE INSERT OR UPDATE
    ON lot
    FOR EACH ROW
EXECUTE FUNCTION validate_auction_time();

COMMIT;

--TODO Forbid updating auctionEnd/auctionStart
-- create or replace function forbid_updating_finished_lots()
--     returns trigger as
-- '
--     begin
--         if (OLD.auction_end < current_timestamp) then
--             raise exception using message = ''update attempt of finished auction'';
--         end if;
--         return NEW;
--     end;
-- ' language plpgsql;
--
-- create or replace trigger auction_lot_finished_check
--     before update
--     ON lot
--     for each row
-- execute function forbid_updating_finished_lots();

