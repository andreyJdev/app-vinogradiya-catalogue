package ru.vinogradiya.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.dto.ProductItemDto;

import java.util.UUID;

public interface ProductsService {

    Page<ProductItemDto> findAll(String search, ProductFilter filter, Pageable pageable);
    ProductItemDto findById(UUID id);
    ProductItemDto save(ProductCreateDto dto);
}