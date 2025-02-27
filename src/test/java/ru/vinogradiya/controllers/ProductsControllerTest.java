package ru.vinogradiya.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.service.ProductsServiceImpl;
import ru.vinogradiya.utils.BaseMvcTest;
import ru.vinogradiya.utils.common.Paged;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ProductsController.class})
class ProductsControllerTest extends BaseMvcTest {

    private static final String URL = "/v1/journal";

    private final List<ProductItemDto> productItemList = Arrays.asList(
            ProductItemDto.builder()
                    .id(1L)
                    .name("Алиса")
                    .selectionMini("США")
                    .build(),
            ProductItemDto.builder()
                    .id(2L)
                    .name("Басанти")
                    .selection(ProductItemDto.Selection.of("Криули"))
                    .build());

    @MockitoBean
    private ProductsServiceImpl service;

    @Test
    @DisplayName("Проверка получения сорта винограда по id, сорт не существует")
    void testFindById_shouldReturnIsNotFound() throws Exception {

        // given
        Mockito.when(service.findById(Mockito.anyLong())).thenReturn(null);

        // when
        var result = mvc.perform(
                get(URL + "/{productId}", 5L));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Проверка получения сорта винограда по id, сорт существует")
    void testFindById_shouldReturnProductItem() throws Exception {

        // given
        var serviceResult = productItemList.get(1);
        Mockito.when(service.findById(Mockito.anyLong())).thenReturn(serviceResult);

        // when
        var result = mvc.perform(
                get(URL + "/{productId}", 2L));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(serviceResult.getName()))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.priceSeed").doesNotExist());
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда")
    void testFindAll_shouldReturnNotEmptyProductItemPaged() throws Exception {

        // given
        var serviceResult = new Paged<>(productItemList, PageRequest.of(0, 2), true);
        Mockito.when(service.findAll(any(), any())).thenReturn(serviceResult);

        // when
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(Collections.emptyList())
                .build();
        var result = mvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(filter))
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Алиса"))
                .andExpect(jsonPath("$.content[0].selection").doesNotExist())
                .andExpect(jsonPath("$.content[1].name").value("Басанти"))
                .andExpect(jsonPath("$.content[1].selection.name").value("Криули"));
    }

    @Test
    @DisplayName("Проверка получения пустого списка сортов винограда")
    void testFindAll_shouldNotReturnEmptyProductItemPaged() throws Exception {

        // given
        var emptyServiceResult = new Paged<ProductItemDto>(Collections.emptyList(), PageRequest.of(0, 2), false);
        Mockito.when(service.findAll(any(), any())).thenReturn(emptyServiceResult);

        // when
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(Collections.emptyList())
                .build();
        var result = mvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(ProductItemFilter.builder().build()))
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andExpect(jsonPath("$.content").isEmpty());
    }
}