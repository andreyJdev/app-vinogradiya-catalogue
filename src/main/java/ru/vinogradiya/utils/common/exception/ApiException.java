package ru.vinogradiya.utils.common.exception;

import lombok.Generated;
import org.assertj.core.util.Arrays;
import ru.vinogradiya.utils.enums.ErrorMessage;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiException extends RuntimeException {

    public static final String MESSAGE = "message";
    private final transient ErrorMessage error;
    private final transient Map<String, Object> details;


    public ApiException(ErrorMessage error, Object... args) {
        this((Throwable) null, (ErrorMessage) error, args);
    }

    public ApiException(Throwable cause, ErrorMessage error, Object... args) {
        this(cause, error, Collections.emptyMap(), args);
    }

    public ApiException(Throwable cause, ErrorMessage error, Map<String, Object> additionalDetails, Object... args) {
        super(cause);
        this.error = error;
        this.details = this.buildDetails(Arrays.asList(args));
        this.details.putAll(additionalDetails);
    }

    public String getMessage() {
        return this.details.get(MESSAGE).toString();
    }

    private Map<String, Object> buildDetails(List<Object> args) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(MESSAGE, args.isEmpty() ? this.error.getMessage() : ru.vinogradiya.utils.common.exception.ExceptionUtil.parseExceptionMessage(this.error.getMessage(), args));
        return result;
    }

    @Generated
    public ErrorMessage getError() {
        return this.error;
    }

    @Generated
    public Map<String, Object> getDetails() {
        return this.details;
    }
}