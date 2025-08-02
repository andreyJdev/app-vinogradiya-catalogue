package ru.vinogradiya.utils.validation.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.vinogradiya.utils.common.string.StringFormater;
import ru.vinogradiya.utils.interceptor.CurrentEntityIdHolder;
import ru.vinogradiya.utils.validation.annotation.UniqueNameConstraint;

import java.util.Optional;
import java.util.UUID;

import static ru.vinogradiya.utils.common.string.MessageUtil.messageSource;
import static ru.vinogradiya.utils.common.string.StringFormater.upperFirstChar;

public class UniqueNameValidator implements ConstraintValidator<UniqueNameConstraint, String> {

    @PersistenceContext
    private EntityManager manager;
    private String id;
    private String table;
    private String column;
    private String message;

    @Override
    public void initialize(UniqueNameConstraint constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
        this.message = messageSource(constraintAnnotation.message());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String query = String.format(
                "SELECT %s FROM %s WHERE LOWER(%s) = LOWER(:value)", id, table, column);
        Object result;
        try {
            result = manager.createNativeQuery(query)
                    .setParameter("value", value)
                    .getSingleResult();
        } catch (NoResultException e) {
            return true;
        }

        Optional<UUID> currentId = CurrentEntityIdHolder.get();
        UUID foundId = UUID.fromString(result.toString());

        if (currentId.isPresent() && foundId.equals(currentId.get())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(StringFormater.stringFormat(message, upperFirstChar(value)))
                .addConstraintViolation();
        return false;
    }
}