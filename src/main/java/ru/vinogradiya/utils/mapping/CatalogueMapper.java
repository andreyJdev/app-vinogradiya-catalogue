package ru.vinogradiya.utils.mapping;

import ru.vinogradiya.models.entity.Product;

public interface CatalogueMapper<T> {

    T toDomain(Product entity);
}