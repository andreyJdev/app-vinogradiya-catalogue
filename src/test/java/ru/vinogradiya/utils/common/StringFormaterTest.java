package ru.vinogradiya.utils.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vinogradiya.utils.common.string.StringFormater;

class StringFormaterTest {

    @Test
    @DisplayName("Должен возвращать строку без изменений")
    void testStringFormat_shouldReturnInputNoChanges() {

        // given
        String input = "St First Second";

        // when
        String result = StringFormater.stringFormat(input, "First");

        // then
        Assertions.assertEquals(input, result);
    }

    @Test
    @DisplayName("Должен возвращать строку с подставленными аргументами")
    void testStringFormat_shouldReturnInputWithChanges() {

        // given
        String input = "St {0} {1}";
        String expected = "St First Second";

        // when
        String result = StringFormater.stringFormat(input, "First", "Second");

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Должен возвращать строку, где первая буква заглавная")
    void testToTitleCase_shouldReturnInputWithTitleCase() {

        // given
        String input = "string";

        // when
        String result = StringFormater.toTitleCase(input);

        // then
        Assertions.assertEquals("String", result);
    }

    @Test
    @DisplayName("Должен возвращать null, если строка null")
    void testToTitleCase_shouldConvertNullToNull() {

        // given
        String input = null;

        // when
        String result = StringFormater.toTitleCase(input);

        // then
        Assertions.assertEquals(input, result);
    }

    @Test
    @DisplayName("Должен возвращать null, если строка пустая")
    void testToTitleCase_shouldConvertBlankToBlank() {

        // given
        String input = "   ";

        // when
        String result = StringFormater.toTitleCase(input);

        // then
        Assertions.assertEquals(input, result);
    }
}