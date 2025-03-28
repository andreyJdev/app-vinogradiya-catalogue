# Сервис ВиноградиЯ каталог

Этот сервис предназначен для взаимодействия с продукцией магазина.

## Стек технологий

Amazon Corretto 17, Docker, PostgreSQL 17, Liquibase 4.31, Maven 3.9.9

## Сборка

Для изменения стандартных параметров Базы Данных настроить init-db.sh. Если параметры были изменены, также поменять соответствующие параметры в docker-compose.yml.

***

Можно скачать последнюю релизную версию приложения, либо должен быть установлен Maven локально. Для компиляции исходников:

Windows

set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vincatalog
set SPRING_DATASOURCE_USERNAME=uvcat
set SPRING_DATASOURCE_PASSWORD=pvcat
mvn clean package

Linux

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vincatalog SPRING_DATASOURCE_USERNAME=uvcat SPRING_DATASOURCE_PASSWORD=pvcat mvn clean package

замените параметры БД (если они были изменены).
Проект соберется только в случае, если присутствует родительский pom.xml микро сервисного приложения.

## Запуск

Чтобы запустить БД, в корневой директории использовать команду
`docker-compose up`.

Для применения миграции
`docker-compose --profile liquibase up`.

Запуск приложения командой
`java -jar target/'имя_исполняемого_файла'.jar`

Взаимодействие через Swagger: http://localhost:8080/swagger-ui/index.html.