package ru.vinogradiya.utils.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vinogradiya.models.dto.ProductCreateDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductItemMapperTest {

    ProductItemMapper mapper = new ProductItemMapper();

    @Test
    @DisplayName("Проверка, если product null, то и dto null")
    void testToDomain_shouldReturnNullIfEntityIsNull() {

        // when
        var result = mapper.toDomain(null);

        // then
        assertNull(result);
    }

    @Test
    @DisplayName("Проверка, что при маппинге Integer из нечислового формата цены пустые")
    void testValidateInteger_shouldReturnPricesIsNullIfNonNumericFormat() {

        // given
        ProductCreateDto createDto = new ProductCreateDto();

        // when
        createDto.setAvailableSeed("test");
        createDto.setAvailableCut(" 4242");

        // then
        Assertions.assertAll(
                () -> assertNull(createDto.getAvailableSeed()),
                () -> assertEquals(4242, createDto.getAvailableCut())
        );
    }

    @Test
    @DisplayName("Проверка, что при маппинге BigDecimal из нечислового формата цены пустые")
    void testValidateBigDecimal_shouldReturnPricesIsNullIfNonNumericFormat() {

        // given
        ProductCreateDto createDto = new ProductCreateDto();

        // when
        createDto.setPriceSeed("test");
        createDto.setPriceCut(" 4242");

        // then
        Assertions.assertAll(
                () -> assertNull(createDto.getPriceSeed()),
                () -> assertEquals(new BigDecimal(4242), createDto.getPriceCut())
        );
    }

    @Test
    @DisplayName("Проверка, что при маппинге BigDecimal < 0.01 цены пустые")
    void testGetFinance_shouldReturnPricesIsNullIfLowValue() {

        // given
        ProductCreateDto createDto = new ProductCreateDto();

        // when
        createDto.setPriceSeed("0.009");
        createDto.setPriceCut("-100");

        // then
        Assertions.assertAll(
                () -> assertNull(createDto.getPriceSeed()),
                () -> assertNull(createDto.getPriceCut())
        );
    }
}