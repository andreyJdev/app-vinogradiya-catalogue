package ru.vinogradiya.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static ru.vinogradiya.utils.common.string.MessageUtil.messageSource;

@Getter
@AllArgsConstructor
public enum ProductErrorMessage implements ErrorMessage {

    PRODUCT_NOT_FOUND("{vinogradiya.catalogue.product.not_found}", HttpStatus.NOT_FOUND, Type.VALIDATION_TYPE);

    private final String message;
    private final HttpStatus status;
    private final Type type;

    @Generated
    public String getMessage() {
        return messageSource(message);
    }
}