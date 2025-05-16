package ru.vinogradiya.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.dto.ProductFilterRequest;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.service.ProductsService;
import ru.vinogradiya.utils.BaseMvcTest;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.ProductErrorMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
class ProductsControllerTest extends BaseMvcTest {

    private static final String REST_URL = "/v1/products";
    private static final UUID ID = UUID.randomUUID();

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

    @Test
    @DisplayName("Проверка получения сорта винограда по id, сорт не существует")
    void testFindById_shouldReturnIsNotFound() throws Exception {

        // given
        UUID productId = UUID.randomUUID();
        Mockito.when(service.findById(productId))
                .thenThrow(new ApiException(ProductErrorMessage.PRODUCT_NOT_FOUND, productId));

        // when
        var result = mvc.perform(
                get(REST_URL + "/{productId}", productId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Проверка получения сорта винограда по id, сорт существует")
    void testFindById_shouldReturnProductItem() throws Exception {

        // given
        ProductItemDto serviceResult = productsSource.get(0);
        Mockito.when(service.findById(ID)).thenReturn(serviceResult);

        // when
        var result = mvc.perform(
                get(REST_URL + "/{productId}", ID));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Велюр"))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.availableSeed").doesNotExist());
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда")
    void testFindAll_shouldReturnNotEmptyProductItemPaged() throws Exception {

        // given
        ProductFilter filter = ProductFilter.builder()
                .selections(null)
                .build();

        ProductFilterRequest request = new ProductFilterRequest();
        request.setSearch(null);
        request.setFilterParams(filter);

        Page<ProductItemDto> serviceResult = new PageImpl<>(productsSource);
        Mockito.when(service.findAll(Mockito.nullable(String.class), any(ProductFilter.class), any(Pageable.class)))
                .thenReturn(serviceResult);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Кузьмич"))
                .andExpect(jsonPath("$.content[1].selection.name").value("Ангуляй Воид Секевич"));
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда с фильтром и поиском")
    void testFindAll_shouldReturnNotEmptyProductPagedWithCustomFilterAndSearch() throws Exception {

        // given
        String selection = "Ангуляй Воид Секевич";
        String search = "-23";
        ProductFilter filter = ProductFilter.builder()
                .selections(List.of(selection))
                .build();

        ProductFilterRequest request = new ProductFilterRequest();
        request.setSearch(search);
        request.setFilterParams(filter);

        Page<ProductItemDto> serviceResult = new PageImpl<>(Collections.singletonList(productsSource.get(1)));
        Mockito.when(service.findAll(Mockito.nullable(String.class), any(ProductFilter.class), any(Pageable.class)))
                .thenReturn(serviceResult);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].selection.name").value(selection))
                .andExpect(jsonPath("$.content[?(@.name=='Велюр')]", hasSize(0)));
    }

    @Test
    @DisplayName("Проверка отправки невалидного фильтра")
    void testFindAll_shouldNotReturnBadRequestStatus() throws Exception {

        // given
        String request = mapper.readValue(root.get("IncorrectJson").toString(), String.class);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.message").value("Невалидный JSON"));
    }
}