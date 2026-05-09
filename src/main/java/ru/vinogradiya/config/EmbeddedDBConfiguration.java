package ru.vinogradiya.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ru.vinogradiya.utils.properties.EmbeddedDBProperties;

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

    private final EmbeddedDBProperties databaseProperties;
    private final LiquibaseProperties liquibaseProperties;

    @Bean
    public DataSource dataSource() throws IOException {
        EmbeddedPostgres pg = EmbeddedPostgres.builder()
                .setPort(databaseProperties.getPort())
                .start();

        initializeDatabase(pg);

        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:" + databaseProperties.getPort() + "/" + databaseProperties.getName() + "?currentSchema=" + databaseProperties.getCurrentSchema())
                .username("postgres")
                .password("")
                .build();
    }

    private void initializeDatabase(EmbeddedPostgres pg) {
        try (Connection conn = pg.getPostgresDatabase().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE " + databaseProperties.getName());
            log.info(">> Embedded DB: БД {} создана", databaseProperties.getName());
        } catch (SQLException e) {
            throw new RuntimeException("Embedded DB: Ошибка при инициализации БД", e);
        }

        try (Connection conn = pg.getDatabase("postgres", databaseProperties.getName()).getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE SCHEMA " + databaseProperties.getCurrentSchema());
            stmt.execute("SET search_path TO " + databaseProperties.getCurrentSchema());
            log.info(">> Embedded DB: Схема " + databaseProperties.getCurrentSchema() + " создана/проверена");
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
                        Objects.requireNonNull(getClass().getResourceAsStream(databaseProperties.getInitialDataPath()))
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
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + databaseProperties.getCurrentSchema() + ".product")) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }
}