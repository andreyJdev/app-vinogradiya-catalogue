package ru.vinogradiya.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.vinogradiya.utils.common.exceptions.ApiException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundWalletException(ApiException e, WebRequest request) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("timestamp", LocalDateTime.now());
        details.put("path", request.getDescription(false).replace("uri=", ""));
        details.put("status", e.getError().getStatus());
        details.put("details", e.getMessage());

        return ResponseEntity.status(e.getError().getStatus()).body(details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(HttpMessageNotReadableException e, WebRequest request) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("timestamp", LocalDateTime.now());
        details.put("path", request.getDescription(false).replace("uri=", ""));
        details.put("status", HttpStatus.BAD_REQUEST.value());
        details.put("details", "Невалидный JSON");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(Exception e, WebRequest request) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("timestamp", LocalDateTime.now());
        details.put("path", request.getDescription(false).replace("uri=", ""));
        details.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        details.put("details", "Внутренняя ошибка сервера");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(details);
    }
}