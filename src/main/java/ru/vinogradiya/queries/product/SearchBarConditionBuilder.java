package ru.vinogradiya.queries.product;

import org.springframework.stereotype.Component;
import ru.vinogradiya.models.entity.Product;

import java.util.Objects;

@Component
public class SearchBarConditionBuilder {

    private static final String ILIKE = " ilike ";
    private static final String OR = " or ";

    public String applySearchBarCondition(String search) {
        if (Objects.isNull(search) || search.isEmpty()) {
            return "";
        }
        search = search.trim();
        return " (" +
                "(" + whereContains(Product.NAME, search) + ")" + OR +
                "(" + whereContains(Product.DESCRIPTION, search) + ")" + OR +
                "(" + whereStartsWith(Product.TIME, search) + ")" + OR +
                "(" + whereStartsWith(Product.STRENGTH, search) + ")" + OR +
                "(" + whereStartsWith(Product.CLUSTER, search) + ")" + OR +
                "(" + whereStartsWith(Product.BERRY, search) + ")" + OR +
                "(" + whereStartsWith(Product.TASTE, search) + ")" + OR +
                "(" + whereStartsWith(Product.SELECTION_MINI, search) + ")" + OR +
                "(" + whereStartsWith(Product.RESISTANCE_COLD, search) + ")" +
                ") ";

    }

    String whereContains(String column, String search) {
        return "cast(%s.%s as varchar)".formatted(Product.TABLE_NAME, column) + ILIKE + "'%" + search + "%'";
    }

    String whereStartsWith(String column, String search) {
        return "cast(%s.%s as varchar)".formatted(Product.TABLE_NAME, column) + ILIKE + "'" + search + "%'";
    }
}