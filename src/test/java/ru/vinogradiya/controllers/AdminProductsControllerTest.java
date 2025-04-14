package ru.vinogradiya.controllers;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import ru.vinogradiya.config.InMemoryDbTestConfig;
import ru.vinogradiya.config.JpaRepositoryTestConfig;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.service.ProductsServiceImpl;
import ru.vinogradiya.utils.BaseMvcTest;
import ru.vinogradiya.utils.common.exception.GlobalExceptionHandler;
import ru.vinogradiya.utils.mapping.ProductsMapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Import({ProductsServiceImpl.class, ProductsMapper.class})
@ContextConfiguration(classes = {AdminProductsController.class, GlobalExceptionHandler.class,
        InMemoryDbTestConfig.class, JpaRepositoryTestConfig.class})
class AdminProductsControllerTest extends BaseMvcTest {

    private static final String REST_URL = "/v1/admin/products";
    private static final UUID ID = UUID.randomUUID();

    private final List<Selection> selectionsSource = List.of(
            new Selection(), new Selection()
    );
    private final List<Product> productsSource = List.of(
            new Product()
    );

    @Autowired
    private EntityManager manager;

    @BeforeEach
    void setUp() {
        selectionsSource.forEach(selection -> selection.setId(UUID.randomUUID()));
        selectionsSource.get(0).setName("Криули");
        selectionsSource.get(0).setProducts(Collections.emptyList());
        selectionsSource.get(1).setName("Ангуляй Воид Секевич");
        selectionsSource.get(1).setProducts(Collections.singletonList(productsSource.get(0)));

        productsSource.get(0).setId(ID);
        productsSource.get(0).setName("Басанти");
        productsSource.get(0).setSelection(selectionsSource.get(0));

        manager.getTransaction().begin();

        manager.createQuery("DELETE FROM Selection").executeUpdate();
        manager.createQuery("DELETE FROM Product").executeUpdate();
        manager.clear();

        selectionsSource.forEach(selection -> manager.persist(selection));
        productsSource.forEach(product -> manager.persist(product));
        manager.flush();
    }

    @AfterEach
    void tearDown() {
        manager.getTransaction().commit();
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