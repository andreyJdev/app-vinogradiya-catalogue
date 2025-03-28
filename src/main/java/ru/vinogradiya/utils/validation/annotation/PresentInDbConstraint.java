package ru.vinogradiya.utils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.vinogradiya.utils.validation.validator.PresentInDbValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PresentInDbValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PresentInDbConstraint {

    String message() default "Задано несуществующее значение поля";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String table();
    String column();
}