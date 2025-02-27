package ru.vinogradiya.utils.common.exceptions;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.List;

@UtilityClass
final class ExceptionUtil {

    public static String parseExceptionMessage(String message, List<Object> args) {
        try {
            Object[] argArray = args.stream().map(Object::toString).toArray();
            var ob = MessageFormat.format(message, argArray);
            return MessageFormat.format(message, argArray);
        } catch (Exception var3) {
            throw new IllegalArgumentException("В процессе парсинга исключения возникла ошибка", var3);
        }
    }
}