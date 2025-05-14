package ru.vinogradiya.utils.validation.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.vinogradiya.models.dto.ProductUpdateDto;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.common.string.StringFormater;
import ru.vinogradiya.utils.validation.annotation.UniqueNameUpdateConstraint;

import java.util.Objects;

import static ru.vinogradiya.utils.common.string.MessageUtil.messageSource;
import static ru.vinogradiya.utils.common.string.StringFormater.toTitleCase;

public class UniqueNameUpdateValidator implements ConstraintValidator<UniqueNameUpdateConstraint, ProductUpdateDto> {

    @PersistenceContext
    private EntityManager manager;
    private String table;
    private String column;
    private String message;

    @Override
    public void initialize(UniqueNameUpdateConstraint constraintAnnotation) {
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
        this.message = messageSource(constraintAnnotation.message());
    }

    @Override
    public boolean isValid(ProductUpdateDto updateDto, ConstraintValidatorContext context) {
        String value = updateDto.getName();
        if (value == null || value.isBlank()) {
            return true;
        }

        String query = String.format("SELECT COUNT(*) FROM %s WHERE LOWER(%s) = LOWER(:value)", table, column);
        Product found = ((Product) manager.createNativeQuery(query)
                .setParameter("value", value)
                .getSingleResult());
        if (found != null && !Objects.equals(found.getId(), updateDto.getId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(StringFormater.stringFormat(message, toTitleCase(value)))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}