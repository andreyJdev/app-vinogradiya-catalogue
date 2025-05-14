package ru.vinogradiya.utils.common.exception;

import lombok.Generated;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vinogradiya.utils.common.dto.ApiError;
import ru.vinogradiya.utils.enums.ErrorMessage;
import ru.vinogradiya.utils.enums.ErrorMessage.Type;
import ru.vinogradiya.utils.enums.GlobalErrorMessage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.vinogradiya.utils.common.string.MessageUtil.messageSource;

@RestControllerAdvice
@NoArgsConstructor
public class GlobalExceptionHandler {

    @Generated
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String OBJECT = "object";
    private static final String FIELD = "field";
    private static final String MESSAGE = "message";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handle(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = this.extractErrors(ex.getBindingResult().getAllErrors());
        ApiError apiError = this.buildFor(ex, HttpStatus.BAD_REQUEST, Type.VALIDATION_TYPE);
        apiError.setDetails(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ApiError> handle(BindException ex) {
        List<Map<String, String>> errors = this.extractErrors(ex.getBindingResult().getAllErrors());
        ApiError apiError = this.buildFor(ex, HttpStatus.BAD_REQUEST, Type.VALIDATION_TYPE);
        apiError.setDetails(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiError> handle(ApiException ex) {
        ApiError apiError = this.buildFor(ex, ex.getError().getStatus(), ex.getError().getType());
        apiError.setDetails(ex.getDetails());
        return ResponseEntity.status(ex.getError().getStatus()).body(apiError);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> handle() {
        return this.handle(new ApiException(GlobalErrorMessage.INVALID_JSON));
    }

    private ApiError buildFor(Exception ex, HttpStatus status, ErrorMessage.Type type) {
        log.error(">> Ошибка при обработке REST запроса", ex);
        ApiError apiError = new ApiError();
        apiError.setUrl(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
        apiError.setStatus(status.value());
        apiError.setError(status.getReasonPhrase());
        apiError.setType(type.getName());
        return apiError;
    }

    private List<Map<String, String>> extractErrors(List<ObjectError> errors) {
        return errors.stream().map(error ->
                FieldError.class.isAssignableFrom(error.getClass()) ? this.toMap((FieldError) error) : this.toMap(error)).toList();
    }

    private Map<String, String> toMap(ObjectError error) {
        return Map.of(OBJECT, (String) Objects.requireNonNullElse(error.getCode(), "UNKNOWN_OBJECT_TYPE"), MESSAGE, (String) Objects.requireNonNullElse(this.extractMessage(error), "UNKNOWN_ERROR"));
    }

    private Map<String, String> toMap(FieldError error) {
        return Map.of(FIELD, (String) Objects.requireNonNullElse(error.getField(), "UNKNOWN_FIELD"), MESSAGE, (String) Objects.requireNonNullElse(this.extractMessage(error), "UNKNOWN_ERROR"));
    }

    private String extractMessage(ObjectError error) {
        return (String) Optional.ofNullable(error.getDefaultMessage()).orElse(messageSource("{vinogradiya.catalogue.exception.validation_error}"));
    }
}