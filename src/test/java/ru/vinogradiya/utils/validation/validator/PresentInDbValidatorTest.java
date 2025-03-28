package ru.vinogradiya.utils.validation.validator;

import jakarta.persistence.EntityManager;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vinogradiya.utils.validation.annotation.PresentInDbConstraint;

@ExtendWith(MockitoExtension.class)
class PresentInDbValidatorTest {

    @InjectMocks
    private final PresentInDbValidator validator = new PresentInDbValidator();
    @Mock
    private EntityManager manager;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
    @Mock
    private PresentInDbConstraint annotation;

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
    @DisplayName("Должен возвращать false, если entityManager не вернет значение из бд")
    void testIsValid_shouldReturnFalseIfEntityManagerDontReturnValue() {

        // given
        String value = "missingValue";
        Query query = Mockito.mock(Query.class);
        Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter("value", value)).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(0L);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(this.violationBuilder);

        // when
        boolean isValid = validator.isValid(value, context);

        // then
        Assertions.assertFalse(isValid);
    }

    @Test
    @DisplayName("Должен возвращать true, если entityManager вернет значение из бд")
    void testIsValid_shouldReturnTrueIfEntityManagerDoReturnValue() {

        // given
        Long value = 2L;
        Query query = Mockito.mock(Query.class);

        Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter("value", value.toString())).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(1L);

        // when
        boolean isValid = validator.isValid(value, context);

        // then
        Assertions.assertTrue(isValid);
    }
}