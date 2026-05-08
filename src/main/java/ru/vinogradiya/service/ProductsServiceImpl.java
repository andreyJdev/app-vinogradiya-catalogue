package ru.vinogradiya.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vinogradiya.models.dto.db.ProductCreateData;
import ru.vinogradiya.models.dto.request.ProductCreateInput;
import ru.vinogradiya.models.dto.request.ProductUpdateInput;
import ru.vinogradiya.models.dto.request.filter.ProductFilter;
import ru.vinogradiya.models.dto.response.ProductItem;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.repositories.ProductsRepository;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.GlobalErrorMessage;
import ru.vinogradiya.utils.enums.ProductErrorMessage;
import ru.vinogradiya.utils.mapping.ItemMapper;

import java.util.UUID;

import static ru.vinogradiya.queries.product.ProductSpecificationBuilder.buildSpecificationFrom;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductsServiceImpl implements ProductsService {

    private final ItemMapper productsMapper;
    private final ProductsRepository repository;

    @Override
    public Page<ProductItem> findAll(String search, ProductFilter filter, Pageable pageable) {
        log.info(">> Запрос на получение всех сортов винограда с фильтром: {} и пагинацией: {}", filter, pageable);
        Page<Product> all = repository.findAll(buildSpecificationFrom(search, filter), pageable);
        log.info(">> Найдено {} записей в журнале", all.getNumberOfElements());
        return all.map(productsMapper::toDomain);
    }

    @Override
    public ProductItem findById(UUID id) {
        log.info(">> Запрос на получение сорта винограда с id: {}", id);
        return repository.findById(id).map(productsMapper::toDomain)
                .orElseGet(() -> {
                    log.warn(">> Элемент с id: {} не найден", id);
                    throw new ApiException(ProductErrorMessage.PRODUCT_NOT_FOUND, id);
                });
    }

    @Override
    @Transactional
    public ProductItem save(ProductCreateInput request) {
        log.info(">> Запрос на добавление сорта винограда с именем: {}", request.getName());
        ProductCreateData data = productsMapper.toCreate(request);
        repository.create(data);
        return repository.findById(data.getId())
                .map(productsMapper::toDomain)
                .orElseThrow(() -> new ApiException(GlobalErrorMessage.INTERNAL_ERROR));
    }

    @Override
    @Transactional
    public ProductItem update(ProductUpdateInput request, UUID id) {
        log.info(">> Запрос на изменения сорта винограда с id: {}", id.toString());
        repository.update(productsMapper.toUpdate(request), id);
        return repository.findById(id)
                .map(productsMapper::toDomain)
                .orElseThrow(() -> new ApiException(ProductErrorMessage.PRODUCT_NOT_FOUND, id));
    }
}