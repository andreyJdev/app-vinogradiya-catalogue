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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({ProductsRepositoryImpl.class})
public class ProductsRepositoryTest extends JpaRepositoryBasedTest {

    private static final Long ID = 9999L;

    private static final Sort sort = Sort.by(Product_.PRICE_SEED).ascending()
            .and(Sort.by(Product_.NAME).ascending());

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ProductsRepository repository;

    private List<Product> products;
    private List<Selection> selections;

    @BeforeEach
    void setUp() {
        selections = Arrays.asList(
                new Selection("Новая"),
                new Selection("Старая"));

        products = Arrays.asList(
                new Product(
                        "Басанти",
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
                        "Оч. ранний",
                        "Басанти",
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
                        "Оч. ранний",
                        "Алиса",
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
        selections.get(0).setId(0L);
        selections.get(1).setId(1L);
        products.get(0).setId(ID);
        products.get(1).setId(1L);
        products.get(2).setId(2L);

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
        Pageable page = PageRequest.of(0, 2, sort);

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
        Pageable page = PageRequest.of(1, 2, sort);

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