package ru.vinogradiya.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.request.ProductCreateInput;
import ru.vinogradiya.models.dto.request.ProductUpdateInput;
import ru.vinogradiya.models.dto.request.filter.ProductFilter;
import ru.vinogradiya.models.dto.response.ProductItem;

import java.util.UUID;

public interface ProductsService {

    Page<ProductItem> findAll(String search, ProductFilter filter, Pageable pageable);
    ProductItem findById(UUID id);
    ProductItem save(ProductCreateInput request);
    ProductItem update(ProductUpdateInput request, UUID id);
}