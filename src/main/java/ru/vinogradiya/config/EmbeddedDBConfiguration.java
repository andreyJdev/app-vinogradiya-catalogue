package ru.vinogradiya.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Profile("db")
@Configuration
@Slf4j
@RequiredArgsConstructor
public class EmbeddedDBConfiguration {

    private static final String DB_NAME = "vincatalog";

    private final LiquibaseProperties liquibaseProperties;

    @Bean
    public DataSource dataSource() throws IOException {
        EmbeddedPostgres pg = EmbeddedPostgres.builder()
                .setPort(5433)
                .start();

        initializeDatabase(pg);

        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5433/" + DB_NAME + "?currentSchema=main")
                .username("postgres")
                .password("")
                .build();
    }

    private void initializeDatabase(EmbeddedPostgres pg) {
        try (Connection conn = pg.getPostgresDatabase().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE " + DB_NAME);
            log.info(">> Embedded DB: БД {} создана", DB_NAME);
        } catch (SQLException e) {
            throw new RuntimeException("Embedded DB: Ошибка при инициализации БД", e);
        }

        try (Connection conn = pg.getDatabase("postgres", DB_NAME).getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE SCHEMA main");
            stmt.execute("SET search_path TO main");
            log.info(">> Embedded DB: Схема main создана/проверена");
        } catch (SQLException e) {
            throw new RuntimeException(">> Embedded DB: Ошибка при инициализации БД", e);
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    private void loadInitialData() throws IOException {
        if (!liquibaseProperties.isEnabled()) {
            return;
        }

        try (Connection conn = dataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            if (isDataWanted(stmt)) {
                String sql = new String(
                        Objects.requireNonNull(getClass().getResourceAsStream("/db/sql-dev-data/initial-data.sql"))
                                .readAllBytes(),
                        StandardCharsets.UTF_8
                );
                stmt.execute(sql);
                log.info(">> Embedded DB: Тестовые данные загружены");
            }
        } catch (Exception e) {
            throw new RuntimeException(">> Embedded DB: Ошибка при загрузке тестовых данных", e);
        }
    }

    private boolean isDataWanted(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM main.product")) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }
}