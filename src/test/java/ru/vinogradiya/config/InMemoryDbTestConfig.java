package ru.vinogradiya.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

@Configuration
@Slf4j
@TestPropertySource(properties = "zonky.test.database.postgres.client.properties.locale=UTF-8")
public class InMemoryDbTestConfig {

    @Value("${zonky.test.database.postgres.client.properties.currentSchema}")
    private String currentSchema;

    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public DataSource dataSource() throws Exception {
        log.info(">> Embedded DB: БД vincatalog создана");
        EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
        EmbeddedPostgres postgres = builder.setConnectConfig("currentSchema", currentSchema).start();
        return postgres.getPostgresDatabase();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "true", matchIfMissing = true)
    public SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        return liquibase;
    }
}