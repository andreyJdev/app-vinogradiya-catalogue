package ru.vinogradiya.utils.enums;

import lombok.Generated;
import org.springframework.http.HttpStatus;

public interface ErrorMessage {

    String getMessage();

    HttpStatus getStatus();

    Type getType();

    enum Type {
        VALIDATION_TYPE("validation"),
        INTEGRATION_TYPE("integration"),
        AUTH_TYPE("auth");

        private final String name;

        @Generated
        public String getName() {
            return name;
        }

        @Generated
        Type(String name) {
            this.name = name;
        }
    }
}