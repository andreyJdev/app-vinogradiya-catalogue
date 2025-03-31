package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SelectionErrorMessage implements ErrorMessage {

    SELECTION_NOT_FOUND("Селекция с идентификатором: {0} не найдена", HttpStatus.NOT_FOUND, Type.VALIDATION_TYPE);

    private final String message;
    private final HttpStatus status;
    private final Type type;
}
