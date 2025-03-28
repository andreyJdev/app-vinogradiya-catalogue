package ru.vinogradiya.utils.common;

import java.text.MessageFormat;

public class StringFormater {

    public static String stringFormat(String input, Object... argArray) {
        return MessageFormat.format(input, argArray);
    }
}