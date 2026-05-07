--liquibase formatted sql
--changeset andrey.jdev@gmail.com:20260507-00-0
--commit Создание таблицы selection

CREATE SCHEMA IF NOT EXISTS main;

CREATE TABLE IF NOT EXISTS main.selection
(
    "id"   uuid         NOT NULL,
    "name" varchar(100) NOT NULL,
    CONSTRAINT selection_pk PRIMARY KEY (id)
);