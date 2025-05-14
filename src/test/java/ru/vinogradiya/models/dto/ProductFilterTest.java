package ru.vinogradiya.models.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductFilterTest {

    ProductFilter filter = new ProductFilter();

    @Test
    @DisplayName("Должен возвращать набор строк в строковом формате")
    void testGetNames_shouldReturnListOfStrings() {

        // given
        List<String> input = Arrays.asList("Басанти", "   ", "Алиса");

        // when
        filter.setNames(input);
        Set<String> result = filter.getNames();

        // then
        Assertions.assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.contains("Басанти")),
                () -> assertTrue(result.contains(null)),
                () -> assertTrue(result.contains("Алиса"))
        );
    }

    @Test
    @DisplayName("Должен возвращать пустой набор, если получен пустой список")
    void testGetNames_shouldReturnEmptySetIfInputListIsEmpty() {

        // when
        filter.setNames(Collections.emptyList());
        Set<String> result = filter.getNames();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Должен возвращать пустой набор, если список не был получен")
    void testGetNames_shouldReturnEmptySetIfInputListIsNull() {

        // when
        filter.setNames(null);
        Set<String> result = filter.getNames();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Должен возвращать набор чисел в строковом формате")
    void testGetResistances_shouldReturnListOfNames() {

        // given
        List<String> input = Arrays.asList("-13", null, "27", "   ", "0");

        // when
        filter.setResistances(input);
        Set<Integer> result = filter.getResistances();

        // then
        Assertions.assertAll(
                () -> assertEquals(4, result.size()),
                () -> assertTrue(result.contains(-13)),
                () -> assertTrue(result.contains(null)),
                () -> assertTrue(result.contains(27)),
                () -> assertTrue(result.contains(0))
        );
    }

    @Test
    @DisplayName("Должен возвращать пустой набор, если получен пустой список")
    void testGetResistances_shouldReturnEmptySetIfInputListIsEmpty() {

        // when
        filter.setResistances(Collections.emptyList());
        Set<Integer> result = filter.getResistances();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Должен возвращать пустой набор, если список не был получен")
    void testGetResistances_shouldReturnEmptySetIfInputListIsNull() {

        // when
        filter.setResistances(null);
        Set<Integer> result = filter.getResistances();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Должен возвращать набор строк в строковом формате")
    void testGetSelections_shouldReturnListOfStrings() {

        // given
        List<String> input = Arrays.asList("Криули", null, "США");

        // when
        filter.setSelections(input);
        Set<String> result = filter.getSelections();

        // then
        Assertions.assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.contains("Криули")),
                () -> assertTrue(result.contains(null)),
                () -> assertTrue(result.contains("США"))
        );
    }
}