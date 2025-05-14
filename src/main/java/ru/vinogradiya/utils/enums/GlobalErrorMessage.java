package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static ru.vinogradiya.utils.common.string.MessageUtil.messageSource;

@Getter
@AllArgsConstructor
public enum GlobalErrorMessage implements ErrorMessage {

    INVALID_JSON("{vinogradiya.catalogue.global.invalid_json}", HttpStatus.BAD_REQUEST, Type.VALIDATION_TYPE),
    INTERNAL_ERROR("{vinogradiya.catalogue.global.internal_server_error}", HttpStatus.INTERNAL_SERVER_ERROR, Type.VALIDATION_TYPE);

    private final String message;
    private final HttpStatus status;
    private final Type type;

    @Generated
    public String getMessage() {
        return messageSource(message);
    }
}