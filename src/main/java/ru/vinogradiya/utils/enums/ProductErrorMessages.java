package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorMessages implements ErrorMessage {

    PRODUCT_NOT_FOUND("Сорт винограда с идентификатором: {0} не найден", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}