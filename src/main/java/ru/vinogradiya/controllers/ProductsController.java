package ru.vinogradiya.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.dto.ProductItemViews;
import ru.vinogradiya.service.ProductsService;
import ru.vinogradiya.utils.common.Paged;

@RestController
@RequestMapping("/v1/journal")
@Tag(name = "products", description = "Управление продуктами")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;

    @GetMapping("{productId:\\d+}")
    @JsonView(ProductItemViews.UserAccess.class)
    @Operation(description = "Получить сорт винограда по идентификатору")
    public ResponseEntity<ProductItemDto> findById(@PathVariable Long productId) {
        ProductItemDto found = service.findById(productId);

        if (found == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(found);
    }

    @PostMapping
    @JsonView(ProductItemViews.UserAccess.class)
    @Operation(description = "Получить все сорта винограда с параметрами")
    public ResponseEntity<Paged<ProductItemDto>> findAll(
            @RequestBody ProductItemFilter filter,
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(value = "priceSeed", direction = Sort.Direction.ASC),
                    @SortDefault(value = "priceCut", direction = Sort.Direction.ASC),
                    @SortDefault(value = "name", direction = Sort.Direction.ASC)
            })
            @ParameterObject Pageable pageable
    ) {
        Paged<ProductItemDto> found = service.findAll(filter, pageable);
        return ResponseEntity.ok(found);
    }
}