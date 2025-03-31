package ru.vinogradiya.repositories;

import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.common.Paged;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepository {

    Paged<Product> findAll(ProductItemFilter filter, Pageable pageable);
    Optional<Product> findById(UUID id);
    Optional<Product> findByName(String name);
    Product create(ProductCreateDto createDto);
}