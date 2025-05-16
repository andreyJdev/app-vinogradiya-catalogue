package ru.vinogradiya.utils.common.exception;

import jakarta.validation.ConstraintViolation;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import ru.vinogradiya.utils.validation.ValidatorProvider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public final class ExceptionUtil {

    public static String parseExceptionMessage(String message, List<Object> args) {
        try {
            Object[] argArray = args.stream().map(Object::toString).toArray();
            var ob = MessageFormat.format(message, argArray);
            return MessageFormat.format(message, argArray);
        } catch (Exception var3) {
            throw new IllegalArgumentException("В процессе парсинга исключения возникла ошибка", var3);
        }
    }

    public static void validate(Object dto) throws BindException {
        Set<ConstraintViolation<Object>> violations = ValidatorProvider.getValidator().validate(dto);
        if (!violations.isEmpty()) {
            BindException e = new BindException(dto, "productUpdateDto");
            violations.forEach(violation ->
                    e.addError(new FieldError("productUpdateDto",
                            Optional.ofNullable(violation.getPropertyPath().toString())
                                    .filter(it -> !it.isBlank())
                                    .orElse("unknown"),
                            violation.getMessage()))
            );
            throw e;
        }
    }
}