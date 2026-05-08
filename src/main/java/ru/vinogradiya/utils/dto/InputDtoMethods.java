package ru.vinogradiya.utils.dto;

import lombok.extern.slf4j.Slf4j;
import ru.vinogradiya.utils.common.string.StringFormater;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
public final class InputDtoMethods {

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
                .map(InputDtoMethods::validateInteger)
                .map(Integer::parseInt)
                .filter(i -> i != 0)
                .orElse(null);
    }

    public static BigDecimal getFinance(String str) {
        return Optional.ofNullable(str)
                .map(InputDtoMethods::validateBigDecimal)
                .map(BigDecimal::new)
                .filter(price -> price.compareTo(BigDecimal.valueOf(0.01)) > 0)
                .orElse(null);
    }

    private static String validateInteger(String str) {
        try {
            String trimmed = str.trim();
            Integer.parseInt(trimmed);
            return trimmed;
        } catch (Exception e) {
            log.warn(">> Неудачная попытка парсинга целочисленного значения {}", str);
            return null;
        }
    }

    private static String validateBigDecimal(String str) {
        try {
            String trimmed = str.trim();
            new BigDecimal(trimmed);
            return trimmed;
        } catch (Exception e) {
            log.warn(">> Неудачная попытка парсинга денежного значения {}", str);
            return null;
        }
    }
}