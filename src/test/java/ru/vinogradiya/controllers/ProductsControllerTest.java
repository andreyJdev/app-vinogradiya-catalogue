package ru.vinogradiya.controllers;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import ru.vinogradiya.config.EntityManagerTestConfig;
import ru.vinogradiya.config.InMemoryDbTestConfig;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.repositories.ProductsRepositoryImpl;
import ru.vinogradiya.service.ProductsServiceImpl;
import ru.vinogradiya.utils.BaseMvcTest;
import ru.vinogradiya.utils.common.exception.GlobalExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ProductsServiceImpl.class, ProductsRepositoryImpl.class})
@ContextConfiguration(classes = {ProductsController.class, InMemoryDbTestConfig.class, EntityManagerTestConfig.class, GlobalExceptionHandler.class})
class ProductsControllerTest extends BaseMvcTest {

    private static final String REST_URL = "/v1/journal";

    private final List<Selection> selectionsSource = List.of(
            new Selection(), new Selection()
    );
    private final List<Product> productsSource = List.of(
            new Product(), new Product(), new Product(), new Product()
    );

    @Autowired
    private EntityManager manager;

    @BeforeEach
    void setUp() {
        IntStream.range(0, selectionsSource.size()).forEach(i -> selectionsSource.get(i).setId((long) i + 1));
        IntStream.range(0, productsSource.size()).forEach(i -> productsSource.get(i).setId((long) i + 1));

        selectionsSource.get(0).setName("Криули");
        selectionsSource.get(0).setProducts(List.of(productsSource.get(0), productsSource.get(1), productsSource.get(2)));
        selectionsSource.get(1).setName("Ангуляй Воид Секевич");
        selectionsSource.get(1).setProducts(Collections.singletonList(productsSource.get(3)));

        productsSource.get(0).setId(1L);
        productsSource.get(0).setName("Ахиллес");
        productsSource.get(0).setSelection(selectionsSource.get(0));
        productsSource.get(1).setId(2L);
        productsSource.get(1).setName("Басанти");
        productsSource.get(1).setSelection(selectionsSource.get(0));
        productsSource.get(2).setId(3L);
        productsSource.get(2).setName("Алиса");
        productsSource.get(2).setSelection(selectionsSource.get(0));
        productsSource.get(3).setId(4L);
        productsSource.get(3).setName("Кузьмич");
        productsSource.get(3).setSelection(selectionsSource.get(1));

        manager.getTransaction().begin();

        manager.createQuery("DELETE FROM Selection").executeUpdate();
        manager.createQuery("DELETE FROM Product").executeUpdate();
        manager.clear();

        selectionsSource.forEach(selection -> manager.persist(selection));
        productsSource.forEach(product -> manager.persist(product));
        manager.flush();

        manager.getTransaction().commit();
    }

    @Test
    @DisplayName("Проверка получения сорта винограда по id, сорт не существует")
    void testFindById_shouldReturnIsNotFound() throws Exception {

        // given
        long productId = 5L;

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
        long productId = 2L;

        // when
        var result = mvc.perform(
                get(REST_URL + "/{productId}", productId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Басанти"))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.priceSeed").doesNotExist());
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда")
    void testFindAll_shouldReturnNotEmptyProductItemPaged() throws Exception {

        // given
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(List.of(""))
                .build();

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(filter))
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(productsSource.size()))
                .andExpect(jsonPath("$.numberOfElements").value(productsSource.size()))
                .andExpect(jsonPath("$.content[3].name").value("Кузьмич"))
                .andExpect(jsonPath("$.content[3].selection.name").value("Ангуляй Воид Секевич"));
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда с изменнеными параметрами")
    void testFindAll_shouldReturnNotEmptyProductItemPagedWithCustomParam() throws Exception {

        // given
        int size = 2;
        String selection = "Криули";
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(List.of(selection))
                .build();

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(filter))
                        .param("page", "0")
                        .param("size", String.valueOf(size))
                        .param("sort", "name,desc"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(size))
                .andExpect(jsonPath("$.content[1].name").value("Ахиллес"))
                .andExpect(jsonPath("$.content[1].selection.name").value(selection));
    }

    @Test
    @DisplayName("Проверка получения пустого списка сортов винограда")
    void testFindAll_shouldNotReturnEmptyProductItemPaged() throws Exception {

        // given
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(List.of("fake"))
                .build();

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(filter))
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Проверка отправки невалидного фильтра")
    void testFindAll_shouldNotReturnBadRequestStatus() throws Exception {

        // given
        String filter = "IncorrectJson";

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(filter))
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.message").value("Невалидный JSON"));
    }
}