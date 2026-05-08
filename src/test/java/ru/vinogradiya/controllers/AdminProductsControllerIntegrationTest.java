package ru.vinogradiya.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.vinogradiya.models.dto.db.ProductCreateData;
import ru.vinogradiya.models.dto.request.ProductCreateInput;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.service.ProductsServiceImpl;
import ru.vinogradiya.utils.IntegrationMvcTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(
        scripts = "classpath:db/sql-test-data/product.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Import({ProductsServiceImpl.class})
class AdminProductsControllerIntegrationTest extends IntegrationMvcTest {

    private static final String REST_URL = "/v1/admin/products";

    List<Selection> selectionsSource = new ArrayList<>();
    List<Product> productsSource = new ArrayList<>();

    @Value("classpath:json/json-test-data/ProductCreateRequest.json")
    Resource createJsonResource;

    @Value("classpath:json/json-test-data/ProductUpdateRequest.json")
    Resource updateJsonResource;

    @BeforeEach
    void setUp() {
        selectionsSource = entityManager.createQuery("SELECT s FROM Selection s", Selection.class)
                .getResultList();
        productsSource = entityManager.createQuery("SELECT s FROM Product s", Product.class)
                .getResultList();
    }

    @Test
    @DisplayName("Проверка добавления сорта в бд, невалидные параметры")
    void testCreate_shouldReturnBadRequest() throws Exception {

        // given
        ProductCreateData dto = new ProductCreateData();
        dto.setName("а");

        // when
        var result = mvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(dto)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка добавления сорта в бд, должен быть создан")
    void testCreate_shouldReturnCreated() throws Exception {

        // given
        ProductCreateInput request = mapper.readValue(createJsonResource.getFile(), ProductCreateInput.class);

        // when
        var result = mvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Проверка обновления сорта в бд, должен быть обновлен")
    void testUpdate_shouldReturnOk() throws Exception {

        // given
        var productId = (UUID) entityManager.createNativeQuery("SELECT id FROM product LIMIT 1")
                .getSingleResult();
        ProductCreateInput request = mapper.readValue(updateJsonResource.getFile(), ProductCreateInput.class);

        // when
        var result = mvc.perform(put(REST_URL + "/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Аналогов нет"));
    }
}