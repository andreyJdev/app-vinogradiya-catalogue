FROM liquibase/liquibase:4.31.0

COPY ./dbscripts/liquibase /dbscripts/liquibase
WORKDIR /dbscripts/liquibase
ENTRYPOINT ["sh", "-c", "liquibase --defaultsFile=liquibase.properties --url=$SPRING_DATASOURCE_URL --username=$SPRING_DATASOURCE_USERNAME --password=$SPRING_DATASOURCE_PASSWORD update"]