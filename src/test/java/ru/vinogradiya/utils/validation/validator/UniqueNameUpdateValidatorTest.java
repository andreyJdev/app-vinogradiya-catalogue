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
import ru.vinogradiya.models.dto.ProductUpdateDto;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.validation.annotation.UniqueNameUpdateConstraint;

@ExtendWith(MockitoExtension.class)
class UniqueNameUpdateValidatorTest {

    @InjectMocks
    private final UniqueNameUpdateValidator validator = new UniqueNameUpdateValidator();
    @Mock
    private EntityManager manager;
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
    @Mock
    private UniqueNameUpdateConstraint annotation;

    @BeforeEach
    public void setUp() {
        Mockito.when(annotation.table()).thenReturn("tableName");
        Mockito.when(annotation.column()).thenReturn("columnName");
        Mockito.when(annotation.message()).thenReturn("default message");
        validator.initialize(annotation);
    }

    @Test
    @DisplayName("Должен возвращать true, если updateDto.name равен null")
    void testIsValid_shouldReturnTrueIfValueIsNull() {

        // given
        ProductUpdateDto updateDto = new ProductUpdateDto(1L);

        // when
        boolean isValid = validator.isValid(updateDto, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Должен возвращать true, если updateDto.name из запроса пуст")
    void testIsValid_shouldReturnTrueIfValueIsBlank(String value) {

        // given
        ProductUpdateDto updateDto = new ProductUpdateDto(1L);
        updateDto.setName(value);

        // when
        boolean isValid = validator.isValid(updateDto, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Должен возвращать false, если updateDto.id = null")
    void testIsValid_shouldReturnFalseIfDtoIdIsNull() {

        // given
        ProductUpdateDto updateDto = new ProductUpdateDto(null);
        updateDto.setName("name");

        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(this.violationBuilder);

        // when
        boolean isValid = validator.isValid(updateDto, context);

        // then
        Assertions.assertFalse(isValid);
    }

    @Test
    @DisplayName("Должен возвращать true, если entityManager не вернет объект из бд (имя свободно)")
    void testIsValid_shouldReturnTrueIfEntityManagerDontReturnProduct() {

        // given
        ProductUpdateDto updateDto = new ProductUpdateDto(1L);
        updateDto.setName("name");
        Query query = Mockito.mock(Query.class);

        Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter("value", updateDto.getName())).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(null);

        // when
        boolean isValid = validator.isValid(updateDto, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Должен возвращать true, если entityManager вернет значение из бд, но id совпадают (имя допустимо, так как не изменилось)")
    void testIsValid_shouldReturnTrueIfEntityManagerDoReturnSelfProduct() {

        // given
        ProductUpdateDto updateDto = new ProductUpdateDto(1L);
        updateDto.setName("Name");
        Product found = new Product();
        found.setId(1L);
        found.setName("Name");

        Query query = Mockito.mock(Query.class);
        Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter("value", updateDto.getName())).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(found);

        // when
        boolean isValid = validator.isValid(updateDto, context);

        // then
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Должен возвращать false, если entityManager вернет значение из бд, но id не совпадают (объект разный, имя одинаковое - нарушение уникальности)")
    void testIsValid_shouldReturnFalseIfEntityManagerDoReturnOtherProductWithEqualName() {

        // given
        ProductUpdateDto updateDto = new ProductUpdateDto(2L);
        updateDto.setName("Name");
        Product found = new Product();
        found.setId(1L);
        found.setName("Name");

        Query query = Mockito.mock(Query.class);
        Mockito.when(manager.createNativeQuery(Mockito.anyString())).thenReturn(query);
        Mockito.when(query.setParameter("value", updateDto.getName())).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(found);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(this.violationBuilder);

        // when
        boolean isValid = validator.isValid(updateDto, context);

        // then
        Assertions.assertFalse(isValid);
    }
}