package ru.vinogradiya.utils.validation.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.vinogradiya.utils.common.StringFormater;
import ru.vinogradiya.utils.validation.annotation.UniqueNameConstraint;

public class UniqueNameValidator implements ConstraintValidator<UniqueNameConstraint, String> {

    @PersistenceContext
    private EntityManager manager;
    private String table;
    private String column;
    private String message;

    @Override
    public void initialize(UniqueNameConstraint constraintAnnotation) {
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = :value", table, column);
        long foundCount = ((Number) manager.createNativeQuery(query)
                .setParameter("value", value)
                .getSingleResult()).longValue();
        if (foundCount != 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(StringFormater.stringFormat(message, value))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}