package ru.vinogradiya.utils;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

@Configuration
@Slf4j
@TestPropertySource(properties = "zonky.test.database.postgres.client.properties.locale=UTF-8")
public class InMemoryDbTestConfig {

    @Bean
    @Primary
    public DataSource inMemoryDS() throws Exception {
        log.info("Using in-memory postgresql database");
        EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
        EmbeddedPostgres postgres = builder.setConnectConfig("currentSchema", "main").start();
        return postgres.getPostgresDatabase();
    }

    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDropFirst(true);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema("public");
        liquibase.setChangeLog("classpath:db/changelog/changelog-test.yml");
        return liquibase;
    }
}