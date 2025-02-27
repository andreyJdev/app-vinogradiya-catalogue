package ru.vinogradiya.repositories;

import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.utils.Paged;

public interface ProductsRepository {

    Paged<Product> findAll(ProductItemFilter filter, Pageable pageable);
}
