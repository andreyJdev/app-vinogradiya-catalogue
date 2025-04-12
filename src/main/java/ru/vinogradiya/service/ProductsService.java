package ru.vinogradiya.service;

import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.utils.common.Paged;

import java.util.UUID;

public interface ProductsService {

    Paged<ProductItemDto> findAll(String search, ProductFilter filter, Pageable pageable);
    ProductItemDto findById(UUID id);
    ProductItemDto save(ProductCreateDto dto);
}