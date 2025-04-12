package ru.vinogradiya.queries.product;

import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Selection;

import java.util.List;
import java.util.Map;

@Component
public class ProductFilterValuersSelectionQuery implements ProductFilterValuesQuery {

    @Override
    public String field() {
        return "selection";
    }

    @Override
    public Map.Entry<Boolean, String> filterCondition(String search) {
        return null;
    }

    @Override
    public Map.Entry<Boolean, String> getWhereForFindAll(ProductFilter filter) {
        List<String> selectionsList = filter.getSelections();
        if (!CollectionUtils.isEmpty(selectionsList)) {
            return Map.entry(Boolean.TRUE, "(%s.%s in (:selectionsList))".formatted(Selection.TABLE_NAME, Selection.NAME));
        }

        return Map.entry(Boolean.FALSE, "");
    }

    @Override
    public void setValueForQuery(ProductFilter filter, Query query) {
        query.setParameter("selectionsList", filter.getSelections());
    }
}