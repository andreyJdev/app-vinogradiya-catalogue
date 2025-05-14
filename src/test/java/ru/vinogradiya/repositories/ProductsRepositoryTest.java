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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.vinogradiya.queries.product.ProductSpecificationBuilder.buildSpecificationFrom;

public class ProductsRepositoryTest extends JpaRepositoryBasedTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME_KUZMICH = "Кузьмич";
    private static final String NAME_ALISA = "Алиса";

    private static final Sort SORT = Sort.by(Product_.PRICE_SEED).ascending()
            .and(Sort.by(Product_.NAME).ascending());

    private final List<Selection> selections = Arrays.asList(
            new Selection(UUID.randomUUID(), "Новая", Collections.emptyList()),
            new Selection(UUID.randomUUID(), "Старая", Collections.emptyList())
    );

    private final List<Product> products = Arrays.asList(
            new Product(
                    ID,
                    "Деф1",
                    "Оч. ранний",
                    "Сильно-рослый",
                    "Крупная 500-1200г.",
                    "36х28 мм 15-20г. розовая",
                    "Мясисто-сочная с мускатным ароматом, оч. сладкая",
                    -23,
                    450,
                    300,
                    "basanti.webp",
                    null,
                    "США",
                    0,
                    0,
                    0,
                    2,
                    selections.get(1)
            ),
            new Product(
                    UUID.randomUUID(),
                    "Деф2",
                    "Оч. ранний",
                    "Сильно-рослый",
                    "Крупная 500-1200г.",
                    "36х28 мм 15-20г. розовая",
                    "Мясисто-сочная с мускатным ароматом, оч. сладкая",
                    -23,
                    500,
                    300,
                    "basanti.webp",
                    null,
                    "США",
                    0,
                    0,
                    0,
                    2,
                    selections.get(0)),
            new Product(
                    UUID.randomUUID(),
                    NAME_KUZMICH,
                    "Оч. ранний",
                    "Сильно-рослый",
                    "Крупная 500-1200г.",
                    "36х28 мм 15-20г. розовая",
                    "Мясисто-сочная с мускатным ароматом, оч. сладкая",
                    -23,
                    400,
                    300,
                    "basanti.webp",
                    null,
                    "США",
                    0,
                    0,
                    0,
                    2,
                    selections.get(0)),
            new Product(
                    UUID.randomUUID(),
                    NAME_ALISA,
                    "Средний",
                    "Сильнорослый",
                    "800-1300г.",
                    "6-9 красная",
                    "Гармоничный мясисто-сочный",
                    -23,
                    700,
                    400,
                    null,
                    null,
                    "США",
                    1,
                    1,
                    1,
                    1,
                    null
            )
    );

    @Autowired
    private ProductsRepository repository;

    @BeforeEach
    void setUp() {
        selections.forEach(selection -> entityManager.persist(selection));
        products.forEach(product -> entityManager.persist(product));
        entityManager.flush();
    }

    @Test
    @DisplayName("Метод findAll должен вернуть правильный сорт при заполненном фильтре")
    void testFindAll_shouldReturnProductWithFilter() {

        // given
        ProductFilter filter = ProductFilter.builder()
                .selections(List.of(
                        selections.get(1).getName()
                ))
                .build();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(null, filter), Pageable.unpaged());

        // then
        Assertions.assertAll(
                () -> assertTrue(result.getContent().contains(products.get(0))),
                () -> assertFalse(result.getContent().contains(products.get(1)))
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
        Assertions.assertAll(
                () -> assertEquals(NAME_KUZMICH, result.getContent().get(0).getName()),
                () -> assertEquals(products.get(0), result.getContent().get(1)),
                () -> assertEquals(2, result.getContent().size())
        );
    }

    @Test
    @DisplayName("Метод findAll должен вернуть вторую страницу в правильном порядке")
    void testFindAll_shouldReturnProductsSecondPageWithOrder() {

        // given
        Pageable page = PageRequest.of(1, 2, SORT);
        ProductFilter filter = ProductFilter.builder().build();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(null, filter), page);

        // then
        Assertions.assertAll(
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(NAME_ALISA, result.getContent().get(1).getName())
        );
    }

    @Test
    @DisplayName("Метод findById должен вернуть Product с заданным id")
    void testFindById_shouldReturnProducts() {

        // when
        Optional<Product> result = repository.findById(ID);

        // then
        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(ID, result.map(Product::getId).orElse(null))
        );
    }

    @Test
    @DisplayName("Метод findById должен вернуть Product с заданным именем сорта")
    void testFindByName_shouldReturnProducts() {

        // when
        Product result = repository.findAllByNameIn(Collections.singletonList(NAME_KUZMICH)).get(0);

        // then
        assertEquals(NAME_KUZMICH, result.getName());
    }

    @Test
    @DisplayName("Метод findAll должен возвращать сорт без селекции")
    void testFindAll_shouldReturnProductWithoutSelection() {

        // given
        ProductFilter filter = ProductFilter.builder().
                selections(Collections.singletonList(null))
                .build();
        Pageable pageable = Pageable.unpaged();

        // when
        Page<Product> result = repository.findAll(buildSpecificationFrom(NAME_ALISA, filter), pageable);

        // then
        Assertions.assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(NAME_ALISA, result.getContent().get(0).getName()),
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
                () -> assertEquals(products.size(), result.getContent().size()),
                () -> assertTrue(result.getContent().contains(products.get(0))),
                () -> assertTrue(result.getContent().contains(products.get(1))),
                () -> assertTrue(result.getContent().contains(products.get(2)))
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

        var query = entityManager.getEntityManager().createQuery(
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
    @Sql("/db/sql-test-data/product.sql")
    @DisplayName("Метод update должен обновить существующий продукт, поле должно быть изменено")
    void testUpdate_shouldUpdateProduct() {

        // given
        String productId = "04f5e733-8680-40a8-b304-33e14d38ad2d";
        String name = "Ангуляй Воид Секевич";

        ProductUpdateDto dto = new ProductUpdateDto(productId);
        dto.setName(name);
        dto.setTime("Изменено");

        var query = entityManager.getEntityManager().createQuery(
                "SELECT p FROM Product p WHERE p.id = :id",
                Product.class
        );
        query.setParameter("id", UUID.fromString(productId));

        // when
        repository.update(dto);
        Product result = query.getSingleResult();

        // then
        Assertions.assertTrue(Objects.nonNull(result));
        Assertions.assertAll(
                () -> assertEquals(name, result.getName()),
                () -> assertEquals("Изменено", result.getTime())
        );
    }
}