package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;
import ru.vinogradiya.utils.common.Paged;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({ProductsRepositoryImpl.class})
public class ProductsRepositoryTest extends JpaRepositoryBasedTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "Алиса";

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
                    600,
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
                    NAME,
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
                    selections.get(0))
    );

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ProductsRepository repository;

    @BeforeEach
    void setUp() {
        selections.forEach(selection -> manager.persist(selection));
        products.forEach(product -> manager.persist(product));
        manager.flush();
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
        Paged<Product> result = repository.findAll(null, filter, Pageable.unpaged());

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

        // when
        Paged<Product> result = repository.findAll(null, null, page);

        // then
        Assertions.assertAll(
                () -> assertEquals(products.get(2), result.getContent().get(0)),
                () -> assertEquals(products.get(1), result.getContent().get(1)),
                () -> assertEquals(2, result.getContent().size())
        );
    }

    @Test
    @DisplayName("Метод findAll должен вернуть вторую страницу в правильном порядке")
    void testFindAll_shouldReturnProductsSecondPageWithOrder() {

        // given
        Pageable page = PageRequest.of(1, 2, SORT);

        // when
        Paged<Product> result = repository.findAll(null, null, page);

        // then
        Assertions.assertAll(
                () -> assertEquals(products.get(0), result.getContent().get(0)),
                () -> assertEquals(1, result.getContent().size())
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
        Optional<Product> result = repository.findByName(NAME);

        // then
        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(NAME, result.map(Product::getName).orElse(null))
        );
    }

    //todo вынести в тест фильтра
    @Test
    @DisplayName("Метод findAll должен возвращать все Product, если все значения фильтра пустые")
    void testFindAll_shouldReturnProductsIfFilterHavaAllNulls() {
        ProductFilter filter = ProductFilter.builder()
                .selections(List.of(""))
                .build();
        Pageable pageable = Pageable.unpaged();

        Paged<Product> result = repository.findAll(null, filter, pageable);

        Assertions.assertEquals(result.getContent(), products);
    }

    //todo вынести в тест фильтра
    @Test
    @DisplayName("Метод findAll должен возвращать все Product, если список в фильтре null")
    void testFindAll_shouldReturnProductsIfFilterListIsNull() {
        ProductFilter filter = ProductFilter.builder()
                .selections(null)
                .build();
        Pageable pageable = Pageable.unpaged();

        Paged<Product> result = repository.findAll(null, filter, pageable);

        Assertions.assertEquals(result.getContent(), products);
    }

    @Test
    void testCreate_shouldReturnProduct() {

        // given
        String name = "Ангуляй Воид Секевич";
        ProductCreateDto dto = new ProductCreateDto();
        dto.setName(name);

        Product expected = repository.create(dto);
        Product result = repository.findByName(name).orElse(null);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expected.getId(), result != null ? result.getId() : null)
        );
    }
}