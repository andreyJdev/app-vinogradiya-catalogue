package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorMessage implements ErrorMessage {

    PRODUCT_NOT_FOUND("Сорт винограда с идентификатором: {0} не найден", HttpStatus.NOT_FOUND, Type.VALIDATION_TYPE);

    private final String message;
    private final HttpStatus status;
    private final Type type;
}