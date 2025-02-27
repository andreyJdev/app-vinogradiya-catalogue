package ru.vinogradiya.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vinogradiya.models.Product;

@RestController
@Tag(name = "products", description = "Управление продуктами")
@RequiredArgsConstructor
public class ProductsController {

    @GetMapping("{productId:\\d+}")
    @Operation(description = "Получить продукт")
    public Product getProductInfo(@PathVariable("productId") Long productId) {
        return new Product();
    }
}
