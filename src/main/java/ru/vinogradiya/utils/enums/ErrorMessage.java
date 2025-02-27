package ru.vinogradiya.utils.enums;

import org.springframework.http.HttpStatus;

public interface ErrorMessage {

    String getMessage();
    HttpStatus getStatus();
}