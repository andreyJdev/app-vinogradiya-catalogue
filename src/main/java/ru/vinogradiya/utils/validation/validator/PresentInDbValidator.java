package ru.vinogradiya.utils.validation.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.vinogradiya.utils.common.StringFormater;
import ru.vinogradiya.utils.validation.annotation.PresentInDbConstraint;

import java.util.UUID;

public class PresentInDbValidator implements ConstraintValidator<PresentInDbConstraint, Object> {

    @PersistenceContext
    private EntityManager manager;
    private String table;
    private String column;
    private String message;

    @Override
    public void initialize(PresentInDbConstraint constraintAnnotation) {
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object objectValue, ConstraintValidatorContext context) {
        if (objectValue == null) {
            return true;
        }

        String value = objectValue.toString();
        if (value.isBlank()) {
            return true;
        }

        Query query = manager.createNativeQuery(String
                .format("SELECT COUNT(*) FROM %s WHERE %s = :value", table, column));
        try {
            query.setParameter("value", UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            query.setParameter("value", value);
        }
        int count = ((Number) query.getSingleResult()).intValue();

        if (count < 1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(StringFormater.stringFormat(message, value))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}