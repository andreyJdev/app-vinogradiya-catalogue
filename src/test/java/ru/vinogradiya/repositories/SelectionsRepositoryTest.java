package ru.vinogradiya.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.vinogradiya.models.dto.db.SelectionCreateData;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.models.entity.Selection_;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Sql("/db/sql-test-data/selection.sql")
public class SelectionsRepositoryTest extends JpaRepositoryBasedTest {

    private static final Sort SORT = Sort.by(Selection_.NAME).ascending();

    private static Map<String, Selection> selections;

    @Autowired
    SelectionsRepository repository;

    @BeforeEach
    void setUp() {
        selections = entityManager.createQuery("SELECT s FROM Selection s", Selection.class)
                .getResultList().stream()
                .collect(
                        Collectors.toMap(Selection::getName, Function.identity())
                );
    }

    @Test
    @DisplayName("Метод create должен создать новую селекцию, при выборке селекция должна быть найдена")
    void testCreate_shouldCreateSelection() {

        // given
        SelectionCreateData data = new SelectionCreateData();

        UUID selectionId = data.getId();
        String name = "Новая селекция";

        data.setName(name);

        var query = entityManager.createQuery(
                "SELECT s FROM Selection s WHERE s.id = :id",
                Selection.class
        );
        query.setParameter("id", selectionId);

        // when
        repository.create(data);

        Selection result = query.getSingleResult();

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(name, result.getName())
        );
    }
}