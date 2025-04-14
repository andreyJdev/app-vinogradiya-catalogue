package ru.vinogradiya.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.repositories.ProductsRepository;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.ProductErrorMessage;
import ru.vinogradiya.utils.mapping.CatalogueMapper;

import java.util.Collections;
import java.util.UUID;

import static ru.vinogradiya.queries.product.ProductSpecificationBuilder.buildSpecificationFrom;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductsServiceImpl implements ProductsService {

    private final CatalogueMapper<ProductItemDto> productsMapper;
    private final ProductsRepository repository;

    @Override
    public Page<ProductItemDto> findAll(String search, ProductFilter filter, Pageable pageable) {
        log.info(">> Запрос на получение всех сортов винограда с фильтром: {} и пагинацией: {}", filter, pageable);
        Page<Product> all = repository.findAll(buildSpecificationFrom(search, filter), pageable);
        log.info(">> Найдено {} записей в журнале", all.getNumberOfElements());
        return all.map(productsMapper::toDomain);
    }

    @Override
    public ProductItemDto findById(UUID id) {
        log.info(">> Запрос на получение сорта винограда с id: {}", id);
        return repository.findById(id).map(productsMapper::toDomain)
                .orElseGet(() -> {
                    log.warn(">> Элемент с id: {} не найден", id);
                    throw new ApiException(ProductErrorMessage.PRODUCT_NOT_FOUND, id);
                });
    }

    @Override
    @Transactional
    public ProductItemDto save(ProductCreateDto dto) {
        log.info(">> Запрос на добавление сорта винограда с именем: {}", dto.getName());
        repository.create(dto);
        return repository.findAllByNameIn(Collections.singletonList(dto.getName()))
                .stream()
                .map(productsMapper::toDomain)
                .toList().get(0);
    }
}