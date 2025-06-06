BEGIN;

-- structure setup

create sequence id_sequence_generator start with 1 increment by 100;

create table category (
                          id bigint not null,
                          parent_id bigint,
                          name varchar(20) not null,
                          primary key (id),
                          constraint UK_CATEGORY_NAME unique (name)
);

alter table if exists category
    add constraint FK_CATEGORY_PARENT_ID
        foreign key (parent_id)
            references category;

-- data setup
-- ...
COMMIT;
