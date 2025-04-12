package ru.vinogradiya.queries.product;

import jakarta.persistence.Query;
import ru.vinogradiya.models.dto.ProductFilter;

import java.util.Map;

public interface ProductFilterValuesQuery {

    String VALUE = "value";
    String TITLE = "title";

    String field();

    Map.Entry<Boolean, String> filterCondition(String search);

    Map.Entry<Boolean, String> getWhereForFindAll(ProductFilter filter);

    void setValueForQuery(ProductFilter filter, Query query);
}