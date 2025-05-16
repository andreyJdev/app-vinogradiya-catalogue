package ru.vinogradiya.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.service.ProductsService;
import ru.vinogradiya.utils.BaseMvcTest;

import java.io.IOException;
import java.util.List;

@Disabled
public class AdminProductsControllerTest extends BaseMvcTest {

    private static final String REST_URL = "/v1/admin/products";

    @MockitoBean
    ProductsService service;

    @Value("classpath:json/json-test-data/ProductItemDto.json")
    Resource resource;

    static JsonNode root;
    static List<ProductItemDto> productsSource;

    @BeforeEach()
    void setUp() throws IOException {
        root = mapper.readTree(resource.getFile());
        productsSource = mapper.readValue(root.get("ProductItemDto").toString(), new TypeReference<>() {});
    }
}