package ru.vinogradiya.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.vinogradiya.models.dto.ProductItemDto;
import ru.vinogradiya.models.dto.ProductItemFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.repositories.ProductsRepositoryImpl;
import ru.vinogradiya.utils.common.Paged;
import ru.vinogradiya.utils.common.exceptions.ApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductsServiceImplTest {

    private final List<Selection> selections = Arrays.asList(
            new Selection(1L, "Новая", Collections.emptyList()),
            new Selection(2L, "Старая", Collections.emptyList())
    );

    private final List<Product> products = Arrays.asList(
            new Product(
                    1L,
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
                    2L,
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
                    selections.get(0))
    );

    @InjectMocks
    private ProductsServiceImpl service;

    @Mock
    private ProductsRepositoryImpl repository;

    @Test
    @DisplayName("Проверка получения от репозитория данных и их преобразования в ProductItemDto")
    void testFindAll_shouldReturnPagedProductItem() {

        // given
        ProductItemFilter filter = ProductItemFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 2);
        Mockito.when(repository.findAll(filter, pageable)).thenReturn(new Paged<>(products));

        // when
        Paged<ProductItemDto> result = service.findAll(filter, pageable);

        // then
        assertAll(
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(2, result.getNumberOfElements()),
                () -> assertEquals(products.get(0).getName(), result.getContent().get(0).getName()),
                () -> assertEquals(products.get(1).getName(), result.getContent().get(1).getName())
        );
        Mockito.verify(repository, Mockito.times(1)).findAll(filter, pageable);
    }

    @Test
    @DisplayName("Проверка получения от репозитория одного элемента и его преобразования в ProductItemDto")
    void testFindById_shouldReturnProductItem() {

        // given
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(products.get(1)));

        // when
        ProductItemDto result = service.findById(2L);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(products.get(1).getName(), result.getName())
        );
    }

    @Test
    @DisplayName("Проверка получения от репозитория несуществующего элемента и его преобразования в Null")
    void testFindById_shouldReturnNull() {

        // given
        Mockito.when(repository.findById(3L)).thenReturn(Optional.empty());

        // when and then
        assertThrows(ApiException.class, () -> service.findById(3L));
    }

    @Test
    @DisplayName("Проверка получения от репозитория одного элемента без селекции и его преобразования в ProductItemDto")
    void testFindById_shouldReturnProductItemWithNullSelection() {

        // given
        Product product = new Product();
        Mockito.when(repository.findById(5L)).thenReturn(Optional.of(product));

        // when
        ProductItemDto result = service.findById(5L);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getSelection())
        );
    }
}