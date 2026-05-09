package ru.vinogradiya.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.vinogradiya.models.dto.response.ProductItem;
import ru.vinogradiya.service.ProductsService;
import ru.vinogradiya.utils.BaseMvcTest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class AdminProductsControllerTest extends BaseMvcTest {

    private static final String REST_URL = "/v1/admin/products";

    @MockitoBean
    ProductsService service;

    @Value("classpath:json/json-test-data/ProductItemDto.json")
    Resource resource;

    static JsonNode root;
    static List<ProductItem> productsSource;

    @BeforeEach()
    void setUp() throws IOException {
        root = mapper.readTree(resource.getFile());
        productsSource = mapper.readValue(root.get("ProductItemDto").toString(), new TypeReference<>() {
        });
    }
}