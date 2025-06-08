BEGIN;

create sequence if not exists id_sequence_generator start with 100 increment by 50;

create table if not exists category
(
    id        bigint      not null,
    parent_id bigint,
    name      varchar(20) not null,
    primary key (id),
    constraint uk_category_name unique (name)
);

alter table if exists category
    add constraint fk_parent
        foreign key (parent_id)
            references category ON DELETE RESTRICT ;


COMMIT;
