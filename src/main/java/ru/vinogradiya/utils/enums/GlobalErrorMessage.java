package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorMessage implements ErrorMessage {

    INVALID_JSON("Невалидный JSON", HttpStatus.BAD_REQUEST, Type.VALIDATION_TYPE);

    private final String message;
    private final HttpStatus status;
    private final Type type;
}