package ru.vinogradiya.utils.validation.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vinogradiya.utils.interceptor.CurrentEntityIdHolder;
import ru.vinogradiya.utils.validation.annotation.UniqueNameConstraint;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UniqueNameValidatorTest {

    @InjectMocks
    private final UniqueNameValidator validator = new UniqueNameValidator();
    @Mock
    private EntityManager manager;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
    @Mock
    private UniqueNameConstraint annotation;

    @BeforeEach
    public void setUp() {
        Mockito.when(annotation.table()).thenReturn("tableName");
        Mockito.when(annotation.column()).thenReturn("columnName");
        Mockito.when(annotation.message()).thenReturn("default message");
        validator.initialize(annotation);
    }

    @Test
    @DisplayName("Должен возвращать true, если value из запроса равен null")
    void testIsValid_shouldReturnTrueIfValueIsNull() {

        // when
        boolean isValid = validator.isValid(null, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Должен возвращать true, если value из запроса пуст")
    void testIsValid_shouldReturnTrueIfValueIsBlank(String value) {

        // when
        boolean isValid = validator.isValid(value, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Должен возвращать true, если entityManager не вернет значение из бд (имя в БД не существует)")
    void testIsValid_shouldReturnTrueIfEntityManagerDontReturnValue() {

        // given
        String value = "presentValue";
        Query query = Mockito.mock(Query.class);

        Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter("value", value)).thenReturn(query);
        Mockito.doThrow(NoResultException.class).when(query).getSingleResult();

        // when
        boolean isValid = validator.isValid(value, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Должен возвращать true, если entityManager вернет значение из бд, и оно совпадает с текущим (имя одинаковое, id одинаковые | PUT запрос)")
    void testIsValid_shouldReturnTrueIfEntityManagerDoReturnValueAndCurrentIdEqual() {

        try (MockedStatic<CurrentEntityIdHolder> mock = Mockito.mockStatic(CurrentEntityIdHolder.class)) {

            // given
            String id = UUID.randomUUID().toString();
            String value = "missingValue";
            Query query = Mockito.mock(Query.class);
            Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
            Mockito.when(query.setParameter("value", value)).thenReturn(query);
            Mockito.when(query.getSingleResult()).thenReturn(id);
            mock.when(CurrentEntityIdHolder::get).thenReturn(Optional.of(UUID.fromString(id)));

            // when
            boolean isValid = validator.isValid(value, context);

            // then
            Assertions.assertTrue(isValid);
        }
    }

    @Test
    @DisplayName("Должен возвращать false, если entityManager вернет значение из бд, но текущая сущность не имеет UUID (имя существует в БД | POST запрос)")
    void testIsValid_shouldReturnTrueIfEntityManagerDoReturnValueAndCurrentIdNotExist() {

        try (MockedStatic<CurrentEntityIdHolder> mock = Mockito.mockStatic(CurrentEntityIdHolder.class)) {

            // given
            String value = "missingValue";
            Query query = Mockito.mock(Query.class);
            Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
            Mockito.when(query.setParameter("value", value)).thenReturn(query);
            Mockito.when(query.getSingleResult()).thenReturn("5e11ca9b-1044-456f-a540-218f46821567");
            mock.when(CurrentEntityIdHolder::get).thenReturn(Optional.empty());
            Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(this.violationBuilder);

            // when
            boolean isValid = validator.isValid(value, context);

            // then
            Assertions.assertFalse(isValid);
        }
    }

    @Test
    @DisplayName("Должен возвращать false, если entityManager вернет значение из бд, но оно не совпадает с текущим id (имя одинаковое, id разные | PUT запрос)")
    void testIsValid_shouldReturnFalseIfEntityManagerDoReturnValueAndCurrentIdNotEqual() {

        try (MockedStatic<CurrentEntityIdHolder> mock = Mockito.mockStatic(CurrentEntityIdHolder.class)) {

            // given
            String foundId = "5e11ca9b-1044-456f-a540-218f46821567";
            String currentId = "675eae27-36c5-479d-ab70-e8fd1bd50411";
            String value = "missingValue";
            Query query = Mockito.mock(Query.class);
            Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
            Mockito.when(query.setParameter("value", value)).thenReturn(query);
            Mockito.when(query.getSingleResult()).thenReturn(foundId);
            mock.when(CurrentEntityIdHolder::get).thenReturn(Optional.of(UUID.fromString(currentId)));
            Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(this.violationBuilder);

            // when
            boolean isValid = validator.isValid(value, context);

            // then
            Assertions.assertFalse(isValid);
        }
    }
}