package ru.vinogradiya.utils.dto;

import ru.vinogradiya.utils.common.string.StringFormater;

import java.util.Optional;

public final class InputDtoMethods {

    private InputDtoMethods() {}

    public static String blankToNull(String str) {
        return Optional.ofNullable(str).filter(s -> !s.isBlank()).orElse(null);
    }

    public static String upperFirst(String str) {
        return Optional.ofNullable(str)
                .map(StringFormater::upperFirstChar)
                .orElse(null);
    }

    public static Integer getNumber(String str) {
        return Optional.ofNullable(str)
                .map(String::trim)
                .map(InputDtoMethods::blankToNull)
                .map(Integer::parseInt)
                .filter(i -> i != 0)
                .orElse(null);
    }
}