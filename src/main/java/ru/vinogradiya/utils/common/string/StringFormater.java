package ru.vinogradiya.utils.common.string;

import java.text.MessageFormat;

public class StringFormater {

    public static String stringFormat(String input, Object... argArray) {
        if (input == null || input.isBlank()) return input;
        return MessageFormat.format(input, argArray);
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isBlank()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}