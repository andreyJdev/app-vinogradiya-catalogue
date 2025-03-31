package ru.vinogradiya.service;

import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.utils.common.Paged;

import java.util.UUID;

public interface ProductsService {

    Paged<ProductItemDto> findAll(ProductItemFilter filter, Pageable pageable);
    ProductItemDto findById(UUID id);
}