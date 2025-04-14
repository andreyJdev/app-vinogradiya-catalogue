package ru.vinogradiya.queries.product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection_;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProductSpecificationBuilder {

    public static Specification<Product> buildSpecificationFrom(String search, ProductFilter filter) {
        return (root, query, builder) -> {
            Predicate searchPredicate = buildSearchPredicate(root, builder, search);
            Predicate filterPredicate = buildFilterPredicate(root, builder, filter);

            return builder.and(filterPredicate, searchPredicate);
        };
    }

    private static Predicate buildFilterPredicate(Root<Product> root, CriteriaBuilder builder, ProductFilter filter) {
        List<Predicate> predicates = new ArrayList<>();

        notNullIn(predicates, root.get(Product_.selection).get(Selection_.NAME), filter.getSelections());

        return builder.and(predicates.toArray(Predicate[]::new));
    }

    private static Predicate buildSearchPredicate(Root<Product> root, CriteriaBuilder builder, String search) {
        if (search == null || search.isEmpty()) return builder.conjunction();
        String containsPattern = "%" + search + "%";
        String startsWithPattern = search + "%";

        List<Predicate> predicates = new ArrayList<>(List.of(
                likeIgnoreCase(root.get(Product_.name), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.description), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.selection).get(Selection_.NAME), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.time), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.strength), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.cluster), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.berry), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.taste), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.selectionMini), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.resistanceCold), builder, startsWithPattern)
        ));
        return builder.or(predicates.toArray(Predicate[]::new));
    }

    private static void notNullIn(List<Predicate> predicates, Path<?> field, List<?> list) {
        if (list == null || list.isEmpty()) return;
        predicates.add(field.in(list));
    }

    private static Predicate likeIgnoreCase(Path<?> field, CriteriaBuilder builder, String pattern) {
        Expression<String> fieldAsString = builder.function("concat", String.class, builder.literal(""), field);
        return builder.like(builder.lower(fieldAsString), pattern.toLowerCase());
    }
}
