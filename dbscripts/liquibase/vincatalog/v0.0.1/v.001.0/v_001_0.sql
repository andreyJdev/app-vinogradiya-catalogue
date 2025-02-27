CREATE TABLE IF NOT EXISTS selection
(
    "id"   bigint       NOT NULL
    CONSTRAINT selection_pk PRIMARY KEY,
    "name" varchar(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS product
(
    "id"              bigint      NOT NULL,
    "name"            varchar(32) NOT NULL,
    "time"            varchar(32),
    "strength"        varchar(32),
    "cluster"         varchar(32),
    "berry"           varchar(64),
    "taste"           varchar(64),
    "resistance_cold" integer,
    "price_seed"      integer,
    "price_cut"       integer,
    "image"           varchar(128),
    "description"     varchar(2048),
    "selection_mini"  varchar(32),
    "available_seed"  integer DEFAULT 0,
    "available_cut"   integer DEFAULT 0,
    "sold_seed"       integer DEFAULT 0,
    "sold_cut"        integer DEFAULT 0,
    "selection_id"    BIGINT,
    CONSTRAINT product_pk PRIMARY KEY (id),
    CONSTRAINT product_name UNIQUE (name)
    );

ALTER TABLE product
    ADD CONSTRAINT product_selection_fk
        FOREIGN KEY (selection_id)
            REFERENCES selection (id)
            ON DELETE SET NULL;

CREATE SEQUENCE selection_sequence
    START WITH 7
    INCREMENT BY 1
    CYCLE;

CREATE SEQUENCE product_sequence
    START WITH 10
    INCREMENT BY 1
    CYCLE;