package ru.vinogradiya.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.dto.ProductFilterRequest;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.service.ProductsServiceImpl;
import ru.vinogradiya.utils.IntegrationMvcTest;
import ru.vinogradiya.utils.mapping.ProductsMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(
        scripts = "classpath:db/sql-test-data/product.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@ContextConfiguration(classes = {
        ProductsServiceImpl.class,
        ProductsMapper.class}
)
public class ProductsControllerIntegrationTest extends IntegrationMvcTest {

    private static final String REST_URL = "/v1/products";

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
    @DisplayName("Проверка получения сорта винограда по id, сорт не существует")
    void testFindById_shouldReturnIsNotFound() throws Exception {

        // given
        UUID productId = UUID.fromString("c5512978-8fc3-4a16-86b8-0861e7c3dec9");

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
        UUID productId = UUID.fromString("c5512978-8fc3-4a16-86b8-0861e7c3dec8");

        // when
        var result = mvc.perform(
                get(REST_URL + "/{productId}", productId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Басанти"))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.availableSeed").doesNotExist());
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда")
    void testFindAll_shouldReturnNotEmptyProductItemPaged() throws Exception {

        // given
        ProductFilter filter = ProductFilter.builder()
                .names(null)
                .selections(null)
                .resistances(null)
                .build();

        ProductFilterRequest request = new ProductFilterRequest();
        request.setSearch(null);
        request.setFilterParams(filter);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(8))
                .andExpect(jsonPath("$.numberOfElements").value(8))
                .andExpect(jsonPath("$.content[3].name").value("Велес к-ш"))
                .andExpect(jsonPath("$.content[3].selection.name").value("Гибридные формы селекции Загорулько В.В."));
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов из всех сортов винограда с измененными параметрами")
    void testFindAll_shouldReturnNotEmptyProductItemPagedWithCustomParam() throws Exception {

        // given
        int size = 100;
        ProductFilter filter = ProductFilter.builder().build();

        ProductFilterRequest request = new ProductFilterRequest();
        request.setSearch(null);
        request.setFilterParams(filter);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
                        .param("page", "0")
                        .param("size", String.valueOf(size))
                        .param("sort", "name,desc"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(productsSource.size()))
                .andExpect(jsonPath("$.content[1].name").value("Цимус к-ш"))
                .andExpect(jsonPath("$.content[1].selection.name").value("Гибридные формы селекции Пысанки О.М."));
    }

    @Test
    @DisplayName("Проверка получения наполненного списка сортов винограда с фильтром и поиском")
    void testFindAll_shouldReturnNotEmptyProductPagedWithCustomFilterAndSearch() throws Exception {

        // given
        List<String> selections = Arrays.asList("Гибридные формы селекции Криули С.И.", "Гибридные формы селекции Калугина В.М.", null);
        String search = "-23";
        ProductFilter filter = ProductFilter.builder()
                .selections(selections)
                .build();

        ProductFilterRequest request = new ProductFilterRequest();
        request.setSearch(search);
        request.setFilterParams(filter);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].selection.name").doesNotExist())
                .andExpect(jsonPath("$.content[1].selection.name").value(selections.get(0)))
                .andExpect(jsonPath("$.content[2].selection.name").value(selections.get(1)))
                .andExpect(jsonPath("$.content[?(@.name=='Велюр')]", hasSize(0)));
    }

    @Test
    @DisplayName("Проверка получения пустого списка сортов винограда")
    void testFindAll_shouldNotReturnEmptyProductItemPaged() throws Exception {

        // given
        ProductFilter filter = ProductFilter.builder()
                .selections(List.of("fake"))
                .build();

        ProductFilterRequest request = new ProductFilterRequest();
        request.setSearch(null);
        request.setFilterParams(filter);

        // when
        var result = mvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
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
        String request = "IncorrectJson";

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