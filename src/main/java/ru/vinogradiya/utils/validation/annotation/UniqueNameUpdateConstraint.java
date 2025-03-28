package ru.vinogradiya.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.vinogradiya.utils.validation.validator.UniqueNameUpdateValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UniqueNameUpdateValidator.class)
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNameUpdateConstraint {

    String message() default "Указанное имя уже существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String table();

    String column();
}