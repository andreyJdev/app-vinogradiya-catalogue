package ru.vinogradiya.utils.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}