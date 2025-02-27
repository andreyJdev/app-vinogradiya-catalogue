package ru.vinogradiya.repositories;

import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.common.Paged;

import java.util.Optional;

public interface ProductsRepository {

    Paged<Product> findAll(ProductItemFilter filter, Pageable pageable);
    Optional<Product> findById(Long id);
}