BEGIN;

create sequence if not exists id_sequence_generator start with 1000 increment by 100;

create table if not exists lot
(
    highest_bid   numeric(38, 2)              not null,
    initial_price numeric(38, 2)              not null,
    auction_end   timestamp(6) with time zone not null,
    auction_start timestamp(6) with time zone not null,
    bids_count    bigint                      not null,
    id            bigint                      not null,
    seller_id     bigint                      not null,
    version       bigint                      not null,
    description   varchar(4000)               not null,
    name          varchar(255)                not null,
    primary key (id),
    check (auction_start < auction_end)
);

create table if not exists lot_categories
(
    categories_id bigint,
    lot_id        bigint not null
);

alter table if exists lot_categories
    add constraint lot_categories_foreign_key
        foreign key (lot_id)
            references lot;

create index if not exists idx_lot_categories ON lot_categories (categories_id);

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

alter table lot
    drop constraint if exists auction_lot_duration_check;
alter table lot
    add constraint auction_lot_duration_check
        check (
--                 (current_timestamp < auction_start and lot.auction_start < auction_end) and
--                 EXTRACT(EPOCH FROM (auction_end - auction_start)) > 3600
            auction_end - auction_start > interval '1 hour'
            );

COMMIT;
