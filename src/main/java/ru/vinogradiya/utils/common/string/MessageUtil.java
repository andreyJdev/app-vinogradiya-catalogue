package ru.vinogradiya.utils.common.string;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

@UtilityClass
public class MessageUtil {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", Locale.getDefault());

    public static final String NUMBER_PATTERN = "^(0|[1-9]\\d*|-[1-9]\\d*)$";
    public static final String POSITIVE_NUMBER_PATTERN = "^(0|[1-9]\\d*)$";
    public static final String POSITIVE_FLOAT_PATTERN = "^(0|[1-9]\\d*)(\\.\\d+)?$";

    public static String messageSource(String message) {
        if (Objects.nonNull(message) && message.startsWith("{") && message.endsWith("}")) {
            String key = message.substring(1, message.length() - 1);
            if (BUNDLE.containsKey(key)) {
                return BUNDLE.getString(key);
            }
        }
        return message;
    }
}