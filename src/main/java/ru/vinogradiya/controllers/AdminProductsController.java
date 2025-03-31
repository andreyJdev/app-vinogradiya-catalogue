package ru.vinogradiya.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.service.ProductsService;

import java.util.Map;

@RestController
@RequestMapping("/v1/admin/products")
@RequiredArgsConstructor
@Tag(name = "adminProducts", description = "Управление продуктами")
public class AdminProductsController {

    private final ProductsService service;

    @PostMapping
    @Operation(description = "Создать новый сорт винограда")
    public ResponseEntity<ProductItemDto> create(@RequestBody @Validated ProductCreateDto dto,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        ProductItemDto response = service.save(dto);
        return ResponseEntity.created(uriComponentsBuilder.
                        replacePath("v1/admin/products/{productId}")
                        .build(Map.of("productId", response.getId())))
                .body(response);
    }
}