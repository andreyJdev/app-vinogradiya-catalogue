package ru.vinogradiya.queries.product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection_;
import ru.vinogradiya.utils.enums.FilterProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductSpecificationBuilder {

    public static Specification<Product> buildSpecificationFrom(String search, ProductFilter filter) {
        return (root, query, builder) -> {
            Predicate searchPredicate = buildSearchPredicate(root, builder, search);
            Predicate filterPredicate = buildFilterPredicate(root, builder, filter);

            return builder.and(filterPredicate, searchPredicate);
        };
    }

    public static Specification<Product> buildSpecificationFrom(FilterProperty property, ProductFilter filter, String search) {
        return (root, query, builder) -> {
            ProductFilter filterWithoutProperty = property.removePropertyFromFilter(filter);

            Predicate filterPredicate = buildFilterPredicate(root, builder, filterWithoutProperty);
            Predicate searchPredicate = buildSearchPredicateByProperty(root, builder, property, search);

            return builder.and(filterPredicate, searchPredicate);
        };
    }

    private static Predicate buildFilterPredicate(Root<Product> root, CriteriaBuilder builder, ProductFilter filter) {
        if (filter == null) return builder.conjunction();
        List<Predicate> predicates = new ArrayList<>();

        isPresentIn(predicates, root.get(Product_.name), builder, filter.getNames());
        isPresentIn(predicates, root.get(Product_.resistanceCold), builder, filter.getResistances());
        isPresentIn(predicates, root.join(Product_.selection, JoinType.LEFT).get(Selection_.name), builder, filter.getSelections());

        return builder.and(predicates.toArray(Predicate[]::new));
    }

    private static Predicate buildSearchPredicate(Root<Product> root, CriteriaBuilder builder, String search) {
        if (search == null || search.isBlank()) return builder.conjunction();
        String containsPattern = "%" + search + "%";
        String startsWithPattern = search + "%";

        List<Predicate> predicates = new ArrayList<>(List.of(
                likeIgnoreCase(root.get(Product_.name), builder, startsWithPattern),
                likeIgnoreCase(root.get(Product_.description), builder, containsPattern),
                likeIgnoreCase(root.join(Product_.selection, JoinType.LEFT).get(Selection_.name), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.time), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.strength), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.cluster), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.berry), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.taste), builder, containsPattern),
                likeIgnoreCase(root.get(Product_.selectionMini), builder, containsPattern),
                equalNumeric(root.get(Product_.resistanceCold), builder, search)
        ));
        return builder.or(predicates.toArray(Predicate[]::new));
    }

    private static Predicate buildSearchPredicateByProperty(Root<Product> root, CriteriaBuilder builder, FilterProperty property, String search) {
        if (search == null || search.isBlank()) return builder.conjunction();
        return likeIgnoreCase(property.getValuePath(root), builder, property.getSearchPattern().formatted(search));
    }

    // Метод поиска точных совпадений в бд, включая null
    private static void isPresentIn(List<Predicate> predicates, Path<?> field, CriteriaBuilder builder, Collection<?> list) {
        if (list == null || list.isEmpty()) return;
        Set<?> nonNullValues = list.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        predicates.add(list.contains(null)
                ? builder.or(field.isNull(), field.in(nonNullValues))
                : field.in(nonNullValues));
    }

    private static Predicate likeIgnoreCase(Path<?> field, CriteriaBuilder builder, String pattern) {
        return builder.like(builder.lower(field.as(String.class)), pattern.toLowerCase());
    }

    private static Predicate equalNumeric(Path<?> field, CriteriaBuilder builder, String pattern) {
        String number = Optional.ofNullable(pattern).filter(n -> !n.isBlank() && n.matches("^(0|[1-9]\\d*|-[1-9]\\d*)$")).orElse(null);
        return builder.equal(field.as(Integer.class), number);
    }
}