create database if not exists test;
use test;

create table kpacs (
    id           int auto_increment primary key,
    title        tinytext null,
    description  varchar(2000) null,
    creationDate date not null default (current_date())
);

create table kpac_sets (
    id    int auto_increment primary key,
    title tinytext null
);

create table kpac_set_connections (
    id      int auto_increment primary key,
    kpac_id int,
    set_id  int,
    constraint foreign key (kpac_id)
        references kpacs (id)
        on delete cascade,
    constraint foreign key (set_id)
        references kpac_sets (id)
        on delete cascade,
    constraint unique (kpac_id, set_id)
);