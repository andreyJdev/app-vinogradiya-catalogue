package ru.vinogradiya.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.vinogradiya.models.dto.FilterValue;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.enums.FilterProperty;

public interface ProductsFilterValuesRepository {

    Page<FilterValue> findDistinctFields(FilterProperty property, Specification<Product> specification, Pageable pageable);
}