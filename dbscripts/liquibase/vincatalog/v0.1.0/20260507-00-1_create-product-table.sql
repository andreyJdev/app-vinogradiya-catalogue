--liquibase formatted sql
--changeset andrey.jdev@gmail.com:20260507-00-1
--commit Создание таблицы product

CREATE TABLE IF NOT EXISTS main.product
(
    "id"              uuid        NOT NULL,
    "name"            varchar(32) NOT NULL,
    "time"            varchar(32),
    "strength"        varchar(32),
    "cluster"         varchar(32),
    "berry"           varchar(64),
    "taste"           varchar(64),
    "resistance_cold" integer,
    "price_seed"      numeric(19, 2),
    "price_cut"       numeric(19, 2),
    "image"           varchar(128),
    "description"     varchar(2048),
    "selection_mini"  varchar(32),
    "available_seed"  integer DEFAULT 0,
    "available_cut"   integer DEFAULT 0,
    "sold_seed"       integer DEFAULT 0,
    "sold_cut"        integer DEFAULT 0,
    "selection_id"    uuid,
    CONSTRAINT product_pk PRIMARY KEY (id),
    CONSTRAINT product_name UNIQUE (name)
);

ALTER TABLE main.product
    ADD CONSTRAINT product_selection_fk
        FOREIGN KEY (selection_id)
            REFERENCES main.selection (id)
            ON DELETE SET NULL;