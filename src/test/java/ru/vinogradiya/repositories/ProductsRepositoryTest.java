package ru.vinogradiya.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.dto.ProductUpdateDto;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static ru.vinogradiya.queries.product.ProductSpecificationBuilder.buildSpecificationFrom;

@Sql("/db/sql-test-data/product.sql")
public class ProductsRepositoryTest extends JpaRepositoryBasedTest {

    private static final Sort SORT = Sort.by(Product_.PRICE_SEED).ascending();

    private static Map<String, Product> products;
    private static Map<String, Selection> selections;

    @Autowired
    private ProductsRepository repository;

    @BeforeEach
    void setUp() {
        selections = entityManager.createQuery("SELECT s FROM Selection s", Selection.class)
                .getResultList().stream()
                .collect(
                        Collectors.toMap(Selection::getName, Function.identity())
                );

        products = entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList().stream()
                .collect(
                        Collectors.toMap(Product::getName, Function.identity())
                );
    }

    @Test
    @DisplayName("Метод findAll должен вернуть правильный сорт при заполненном фильтре")
    void testFindAll_shouldReturnProductWithFilter() {

        // given
        Selection filterSelection = selections.get("Гибридные формы селекции Криули С.И.");
        ProductFilter filter = ProductFilter.builder()
                .selections(List.of(
                        filterSelection.getName()
                ))
                .build();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(null, filter), Pageable.unpaged());

        // then
        Assertions.assertAll(
                () -> assertTrue(result.getContent().contains(products.get("Велюр"))),
                () -> assertTrue(result.getContent().contains(products.get("Рембо"))),
                () -> assertFalse(result.getContent().contains(products.get("Минор")))
        );
    }

    @Test
    @DisplayName("Метод findAll должен вернуть первую страницу в правильном порядке")
    void testFindAll_shouldReturnProductsFirstPageWithOrder() {

        // given
        Pageable page = PageRequest.of(0, 2, SORT);
        ProductFilter filter = ProductFilter.builder().build();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(null, filter), page);

        // then
        System.out.println();
        Assertions.assertAll(
                () -> assertEquals(products.get("Минор"), result.getContent().get(0)),
                () -> assertEquals(products.get("Рембо"), result.getContent().get(1)),
                () -> assertEquals(2, result.getContent().size())
        );
    }

    @Test
    @DisplayName("Метод findAll должен вернуть вторую страницу в правильном порядке")
    void testFindAll_shouldReturnProductsSecondPageWithOrder() {

        // given
        Pageable page = PageRequest.of(2, 4, SORT);
        ProductFilter filter = ProductFilter.builder().build();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(null, filter), page);

        // then
        System.out.println();
        Assertions.assertAll(
                () -> assertEquals(products.get("Аркадия"), result.getContent().get(0)),
                () -> assertEquals(1, result.getContent().size())
        );
    }

    @Test
    @DisplayName("Метод findById должен вернуть Product с заданным id")
    void testFindById_shouldReturnProducts() {

        // given
        UUID productId = UUID.fromString("04f5e733-8680-40a8-b304-33e14d38ad2d");

        // when
        Optional<Product> result = repository.findById(productId);

        // then
        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(productId, result.map(Product::getId).orElse(null))
        );
    }

    @Test
    @DisplayName("Метод findById должен вернуть Product с заданным именем сорта")
    void testFindByName_shouldReturnProducts() {

        // given
        String productName = "Минор";

        // when
        Product result = repository.findAllByNameIn(Collections.singletonList(productName)).get(0);

        // then
        assertEquals(productName, result.getName());
    }

    @Test
    @DisplayName("Метод findAll должен возвращать сорт без селекции")
    void testFindAll_shouldReturnProductWithoutSelection() {

        // given
        String search = "Аркадия";
        ProductFilter filter = ProductFilter.builder().
                selections(Collections.singletonList(null))
                .build();
        Pageable pageable = Pageable.unpaged();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(search, filter), pageable);

        // then
        Assertions.assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(search, result.getContent().get(0).getName()),
                () -> assertNull(result.getContent().get(0).getSelection())
        );
    }

    @Test
    @DisplayName("Метод findAll должен возвращать все Product, если список в фильтре null")
    void testFindAll_shouldReturnProductsIfFilterListIsNull() {

        // given
        ProductFilter filter = ProductFilter.builder()
                .selections(null)
                .build();
        Pageable pageable = Pageable.unpaged();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(null, filter), pageable);

        // then
        Assertions.assertAll(
                () -> assertEquals(products.size(), result.getContent().size())
        );
    }

    @Test
    @DisplayName("Метод create должен создать новый продукт, при выборке продукт должен быть найден")
    void testCreate_shouldCreateProduct() {

        // given
        ProductCreateDto dto = new ProductCreateDto();

        UUID productId = dto.getId();
        String name = "Ангуляй Воид Секевич";

        dto.setName(name);
        dto.setTime("Время созревания");
        dto.setStrength("Сила роста");
        dto.setCluster("Гроздь");
        dto.setBerry("Ягода");
        dto.setTaste("Вкус");
        dto.setResistanceCold("-32");
        dto.setPriceSeed("500");
        dto.setPriceCut("700");
        dto.setImage("img.png");
        dto.setDescription("Описание");
        dto.setSelectionMini("Мини селекция");
        dto.setAvailableSeed("5");
        dto.setAvailableCut("5");
        dto.setSoldSeed("2");
        dto.setSoldCut("3");

        var query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.id = :id",
                Product.class
        );
        query.setParameter("id", productId);

        // when
        repository.create(dto);
        Product result = query.getSingleResult();

        // then
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(name, result.getName())
        );
    }

    @Test
    @DisplayName("Метод update должен обновить существующий продукт, поле должно быть изменено")
    void testUpdate_shouldUpdateProduct() {

        // given
        String productId = "04f5e733-8680-40a8-b304-33e14d38ad2d";
        String selectionId = "45283f75-af8b-4e71-b5ae-38ab6c613f1a";
        String name = "Ангуляй Воид Секевич";

        ProductUpdateDto dto = new ProductUpdateDto();
        dto.setId(productId);
        dto.setName(name);
        dto.setSelectionId(selectionId);
        dto.setTime("Изменено");

        var query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.id = :id",
                Product.class
        );
        query.setParameter("id", UUID.fromString(productId));

        // when
        repository.update(dto);

        entityManager.clear();
        Product result = query.getSingleResult();

        // then
        Assertions.assertTrue(Objects.nonNull(result));
        Assertions.assertAll(
                () -> assertEquals(name, result.getName()),
                () -> assertEquals("Изменено", result.getTime())
        );
    }
}