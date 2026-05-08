package ru.vinogradiya.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vinogradiya.models.dto.request.ProductCreateInput;
import ru.vinogradiya.models.dto.request.ProductUpdateInput;
import ru.vinogradiya.models.dto.response.ProductItem;
import ru.vinogradiya.service.ProductsService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/products")
@RequiredArgsConstructor
@Tag(name = "adminProducts", description = "Управление продуктами")
public class AdminProductsController {

    private final ProductsService service;

    @PostMapping
    @Operation(description = "Создать новый сорт винограда")
    public ResponseEntity<ProductItem> create(@RequestBody @Validated ProductCreateInput request,
                                              UriComponentsBuilder uriComponentsBuilder) {
        ProductItem response = service.save(request);
        return ResponseEntity.created(uriComponentsBuilder.
                        replacePath("v1/admin/products/{productId}")
                        .build(Map.of("productId", response.getId())))
                .body(response);
    }

    @PutMapping("{productId}")
    @Operation(description = "Обновить имеющийся сорт винограда")
    public ResponseEntity<ProductItem> update(@PathVariable("productId") UUID id,
                                              @RequestBody @Validated ProductUpdateInput request) {
        ProductItem response = service.update(request, id);
        return ResponseEntity.ok(response);
    }
}