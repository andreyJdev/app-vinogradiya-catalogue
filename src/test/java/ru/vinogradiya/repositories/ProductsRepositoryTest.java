package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Product_;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;
import ru.vinogradiya.utils.Paged;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({ProductsRepositoryImpl.class})
public class ProductsRepositoryTest extends JpaRepositoryBasedTest {

    private static final Long ID = 9999L;

    private static final Sort SORT = Sort.by(Product_.PRICE_SEED).ascending()
            .and(Sort.by(Product_.NAME).ascending());

    private List<Selection> selections = Arrays.asList(
            new Selection(1L, "Новая", Collections.emptyList()),
            new Selection(2L, "Старая", Collections.emptyList())
    );

    private List<Product> products = Arrays.asList(
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
                    1L,
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
                    2L,
                    "Алиса",
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
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(List.of(
                        selections.get(1).getName()
                ))
                .build();

        // when
        Paged<Product> result = repository.findAll(filter, Pageable.unpaged());

        // then
        assertAll(
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
        Paged<Product> result = repository.findAll(null, page);

        // then
        assertAll(
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
        Paged<Product> result = repository.findAll(null, page);

        // then
        assertAll(
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
        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(ID, result.map(Product::getId).orElse(null))
        );
    }

    //todo вынести в тест фильтра
    @Test
    @DisplayName("Метод findAll должен возвращать все Product, если все значения фильтра пустые")
    void testFindAll_shouldReturnProductsIfFilterHavaAllNulls() {
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(List.of(""))
                .build();
        Pageable pageable = Pageable.unpaged();

        Paged<Product> result = repository.findAll(filter, pageable);

        assertEquals(result.getContent(), products);
    }

    //todo вынести в тест фильтра
    @Test
    @DisplayName("Метод findAll должен возвращать все Product, если список в фильтре null")
    void testFindAll_shouldReturnProductsIfFilterListIsNull() {
        ProductItemFilter filter = ProductItemFilter.builder()
                .selections(null)
                .build();
        Pageable pageable = Pageable.unpaged();

        Paged<Product> result = repository.findAll(filter, pageable);

        assertEquals(result.getContent(), products);
    }
}