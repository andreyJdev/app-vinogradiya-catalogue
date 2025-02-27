#!/bin/bash
set -e

# Создание пользователя
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  CREATE USER uvcat WITH ENCRYPTED PASSWORD 'pvcat';
EOSQL

# Настройка БД: создание схемы main и назначение прав
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=vincatalog <<-EOSQL
  CREATE SCHEMA main;
  ALTER ROLE uvcat SET search_path TO main;
  GRANT ALL PRIVILEGES ON SCHEMA main TO uvcat;
EOSQL