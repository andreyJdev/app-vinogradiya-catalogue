package ru.vinogradiya.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.repositories.ProductsRepository;
import ru.vinogradiya.utils.common.Paged;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.ProductErrorMessage;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository repository;

    @Override
    public Paged<ProductItemDto> findAll(ProductItemFilter filter, Pageable pageable) {
        log.info(">> Запрос на получение всех сортов винограда с фильтром: {} и пагинацией: {}", filter, pageable);
        Paged<Product> all = repository.findAll(filter, pageable);
        log.info(">> Найдено {} записей в журнале", all.getNumberOfElements());
        return all.map(this::buildProductItemDto);
    }

    @Override
    public ProductItemDto findById(UUID id) {
        log.info(">> Запрос на получение сорта винограда с id: {}", id);
        return repository.findById(id).map(this::buildProductItemDto)
                .orElseGet(() -> {
                    log.warn(">> Элемент с id: {} не найден", id);
                    throw new ApiException(ProductErrorMessage.PRODUCT_NOT_FOUND, id);
                });
    }

    private ProductItemDto buildProductItemDto(Product p) {
        ProductItemDto.Selection selection = Objects.nonNull(p.getSelection()) && Objects.nonNull(p.getSelection().getName()) ?
                ProductItemDto.Selection.of(p.getSelection().getName()) :
                null;
        return ProductItemDto.builder()
                .id(p.getId())
                .name(p.getName())
                .selection(selection)
                .time(p.getTime())
                .strength(p.getStrength())
                .cluster(p.getCluster())
                .berry(p.getBerry())
                .taste(p.getTaste())
                .resistanceCold(p.getResistanceCold())
                .priceSeed(p.getPriceSeed())
                .priceCut(p.getPriceCut())
                .image(p.getImage())
                .description(p.getDescription())
                .selectionMini(p.getSelectionMini())
                .availableSeed(p.getAvailableSeed())
                .availableCut(p.getAvailableCut())
                .soldCut(p.getSoldCut())
                .soldSeed(p.getSoldSeed())
                .build();
    }
}