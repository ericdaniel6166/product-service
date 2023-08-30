
create table if not exists product
(
    id                 bigserial
        primary key,
    created_by         varchar(255),
    created_date       timestamp(6),
    last_modified_by   varchar(255),
    last_modified_date timestamp(6),
    description        varchar(255),
    name               varchar(255),
    price              numeric(19, 4)
);