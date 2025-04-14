package ru.vinogradiya.utils.enums;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection_;

import java.util.List;

import static ru.vinogradiya.utils.enums.FilterProperty.Constants.CONSTANTS_PATTERN;
import static ru.vinogradiya.utils.enums.FilterProperty.Constants.STARTS_WITH_PATTERN;

@Getter
@RequiredArgsConstructor
public enum FilterProperty {

    NAME(List.of(Product_.NAME), List.of(), STARTS_WITH_PATTERN),
    RESISTANCE_COLD(List.of(Product_.RESISTANCE_COLD), List.of(), STARTS_WITH_PATTERN),
    SELECTION(List.of(Product_.SELECTION, Selection_.NAME), List.of(), CONSTANTS_PATTERN);

    private final List<String> valuePath;
    private final List<String> titlePath;
    private final String searchPattern;

    public ProductFilter removePropertyFromFilter(ProductFilter property) {
        switch (this) {
            case NAME -> property.setNames(null);
            case SELECTION -> property.setSelections(null);
        }
        return property;
    }

    public List<Selection<String>> getSelection(Root<Product> root, CriteriaBuilder builder) {
        if (!this.getTitlePath().isEmpty()) {
            return List.of(buildFieldPath(root).as(String.class).alias("value"), buildSelectionPath(root).as(String.class).alias("title"));
        }
        return List.of(buildFieldPath(root).as(String.class).alias("value"), builder.literal("").alias("title"));
    }

    private Path<Product> buildFieldPath(Root<Product> root) {
        Path<Product> path = root;
        for (String singleValue : valuePath) {
            path = path.get(singleValue);
        }
        return path;
    }

    public Path<Product> getValuePath(Root<Product> root) {
        return buildFieldPath(root);
    }

    public Path<Product> getTitlePath(Root<Product> root) {
        return buildSelectionPath(root);
    }

    private Path<Product> buildSelectionPath(Root<Product> root) {
        Path<Product> path = root;
        for (String singleTitle : titlePath) {
            path = path.get(singleTitle);
        }
        return path;
    }

    public static class Constants {

        private Constants() {
        }

        public static final String STARTS_WITH_PATTERN = "%s%%";
        public static final String CONSTANTS_PATTERN = "%%%s%%";
    }
}