package ru.vinogradiya.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.service.ProductsServiceImpl;
import ru.vinogradiya.utils.IntegrationMvcTest;
import ru.vinogradiya.utils.mapping.ProductsMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(
        scripts = "classpath:db/sql-test-data/product.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Import({ProductsServiceImpl.class, ProductsMapper.class})
class AdminProductsControllerIntegrationTest extends IntegrationMvcTest {

    private static final String REST_URL = "/v1/admin/products";

    List<Selection> selectionsSource = new ArrayList<>();
    List<Product> productsSource = new ArrayList<>();

    @BeforeEach
    void setUp() {
        selectionsSource = entityManager.createQuery("SELECT s FROM Selection s", Selection.class)
                .getResultList();
        productsSource = entityManager.createQuery("SELECT s FROM Product s", Product.class)
                .getResultList();
    }

    @Test
    @DisplayName("Проверка добавления сорта в бд, валидные параметры")
    void testCreate_shouldReturnCreated() throws Exception {

        // given
        String name = "Алиса";
        ProductCreateDto dto = new ProductCreateDto();
        dto.setName(name);
        dto.setSelectionId("  ");

        // when
        var result = mvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(dto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.selection").doesNotExist());
    }

    @Test
    @DisplayName("Проверка добавления сорта в бд, невалидные параметры")
    void testCreate_shouldReturnBadRequest() throws Exception {

        // given
        ProductCreateDto dto = new ProductCreateDto();
        dto.setName("а");

        // when
        var result = mvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(dto)));

        // then
        result.andExpect(status().isBadRequest());
    }
}