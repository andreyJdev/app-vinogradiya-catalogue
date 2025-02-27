package ru.vinogradiya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.Data;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;

import java.util.List;
import java.util.Objects;

@Data
public class ProductItemFilter {

    @Schema(description = "Название селекции")
    String selection;

    /*public void applyConditions(List<Predicate> predicates, CriteriaBuilder builder, Root<Product> product) {
        if (Objects.nonNull(selection) && !selection.isBlank()) {
            predicates.add(builder.equal(product.get(Product_.selection.getName()), selection));
        }
    }

    public void applySelection(List<Selection<?>> selection, CriteriaBuilder builder, Root<Product> product) {
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        R
    }*/
}
